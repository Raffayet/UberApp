import requests
import json
from locust import HttpUser, task, between, events, run_single_user
from gevent.lock import Semaphore

ride_ids = []
active_users = 0
lock = Semaphore()


class QuickstartUser(HttpUser):
    host = 'http://localhost:8081/api'
    wait_time = between(1, 2)


    def on_start(self):
        headers = {'Content-Type': 'application/json; charset=utf-8'}
        response = self.client.post("/auth/login", json={"email": "sasalukic@gmail.com", "password": "sasa123"},
                                    headers=headers)
        accessToken = json.loads(response.text)["accessToken"]
        self.header = {'Authorization': 'Bearer ' + accessToken, 'Content-Type': 'application/json; charset=utf-8'}
        self.ride = {}

    @task(1)
    def get_active_rides(self):
        global ride_ids, active_users
        if not self.ride:
            self.coordinates = []
            self.active_rides = self.client.get('/rides/active-rides', headers=self.header).json()
            # print(self.active_rides)
            if len(self.active_rides) == 0 or not self.active_rides:
                return

            active_users += 1
            lock.wait()
            lock.acquire()
            try:
                for active_ride in self.active_rides:
                    if active_ride["id"] not in ride_ids:
                        print("Uzeo ride sa id-em:"+str(active_ride["id"]))
                        self.ride = active_ride
                        self.reset_state()
                        self.driving_to_start_point = True
                        self.get_new_coordinates_to_start_route()
                        ride_ids.append(active_ride["id"])
                        break
            finally:
                lock.release()

    @task(3)
    def update_driver_position(self):
        if not self.ride:
            return

        if len(self.coordinates) > 0:
            new_coordinate = self.coordinates.pop(0)
            latitude = new_coordinate["lat"] if 'lat' in new_coordinate.keys() else new_coordinate["latitude"]
            longitude = new_coordinate["lng"] if 'lng' in new_coordinate.keys() else new_coordinate["longitude"]

            self.ride["driver"]["latitude"] = latitude
            self.ride["driver"]["longitude"] = longitude
            # azuriraj poziciju vozaca u voznji i obavesti klijente
            response = self.client.put(f"/map/", json=self.ride, headers=self.header)
            self.ride = response.json()
            ride_status = self.ride["status"]
            if ride_status == "CANCELED":
                self.ride = {}
                self.reset_state()
        elif len(self.coordinates) == 0 and self.driving_to_start_point:
            # Zavrsio je voznju do pocetne tacke voznje i sad zapocinje voznju
            self.reset_state()
            self.driving_the_route = True
            self.coordinates = self.ride["atomicPoints"]

        elif len(self.coordinates) == 0 and self.driving_the_route:
            self.end_ride()
            self.reset_state()
            self.finished_driving_route = True
            self.ride = {}

    def get_new_coordinates_to_start_route(self):
        driver = self.ride['driver']
        start_point = self.ride["atomicPoints"][0]
        driver_cordinates = {'id': 0, 'lat': driver["latitude"], 'lng': driver["longitude"]}
        ride_start_point_cordinates = {'id': 0, 'lat': start_point["latitude"],
                                       'lng': start_point["longitude"]}

        response = self.client.post("/map/determine-optimal-route",
                                    json=[driver_cordinates, ride_start_point_cordinates],
                                    headers=self.header)

        self.routeGeoJSON = response.json()
        self.coordinates = self.routeGeoJSON["atomicPoints"]
        self.ride["atomicPointsBeforeRide"] = self.changeToPoints(self.routeGeoJSON["atomicPoints"])

    def reset_state(self):
        self.driving_to_start_point = False
        self.driving_the_route = False
        self.finished_driving_route = False

    def end_ride(self):
        self.client.put(f"/rides/{self.ride['id']}", headers=self.header)

    def changeToPoints(self, atomicPoints):
        points = []
        for point in atomicPoints:
            newPoint = {"latitude": point["lat"], "longitude": point["lng"]}
            points.append(newPoint)
        return points
