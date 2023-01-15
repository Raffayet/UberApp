import requests
import json

from locust import HttpUser, task, between, events, run_single_user
from random import randrange

start_and_end_points = [
    (45.235866, 19.807387),  # Djordja Mikeša 2
    (45.247309, 19.796717),  # Andje Rankovic 2
    (45.259711, 19.809787),  # Veselina Maslese 62
    (45.261421, 19.823026),  # Jovana Hranilovica 2
    (45.265435, 19.847805),  # Bele njive 24
    (45.255521, 19.845071),  # Njegoseva 2
    (45.249241, 19.852152),  # Stevana Musica 20
    (45.242509, 19.844632),  # Boska Buhe 10A
    (45.254366, 19.861088),  # Strosmajerova 2
    (45.223481, 19.847990)  # Gajeva 2
]

taxi_stops = [
    (45.238548, 19.848225),  # Stajaliste na keju
    (45.243097, 19.836284),  # Stajaliste kod limanske pijace
    (45.256863, 19.844129),  # Stajaliste kod trifkovicevog trga
    (45.255055, 19.810161),  # Stajaliste na telepu
    (45.246540, 19.849282)  # Stajaliste kod velike menze
]

license_plates = [
    'NS-001-AA',
    'NS-001-AB',
    'NS-001-AC'
]

ride_ids = []


#
# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
#     requests.delete('http://localhost:8081/api/ride')
#     requests.delete('http://localhost:8081/api/vehicle')

class QuickstartUser(HttpUser):
    host = 'http://localhost:8081/api'
    wait_time = between(1, 2)
    taken_ride_ids = []

    def on_start(self):
        headers = {'Content-Type': 'application/json; charset=utf-8'}
        response = self.client.post("/auth/login", json={"email": "sasalukic@gmail.com", "password": "sasa123"}, headers=headers)
        accessToken = json.loads(response.text)["accessToken"]
        self.header = {'Authorization': 'Bearer '+accessToken, 'Content-Type': 'application/json; charset=utf-8'}
        self.ride = {}

    @task(1)
    def get_active_rides(self):
        if not self.ride:
            self.coordinates = []
            self.active_rides = self.client.get('/rides/active-rides', headers=self.header).json()
            # print(self.active_rides)
            if len(self.active_rides) == 0 or not self.active_rides:
                return
            if self.active_rides[0]["id"] not in ride_ids:
                self.ride = self.active_rides[0]
                self.reset_state()
                self.driving_to_start_point = True
                self.get_new_coordinates_to_start_route()
                ride_ids.append(self.ride["id"])

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
            self.client.put(f"/map/", json=self.ride, headers=self.header)
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

        response = self.client.post("/map/determine-optimal-route", json=[driver_cordinates, ride_start_point_cordinates],
                                 headers=self.header)

        self.routeGeoJSON = response.json()
        self.coordinates = self.routeGeoJSON["atomicPoints"]

    def reset_state(self):
        self.driving_to_start_point = False
        self.driving_the_route = False
        self.finished_driving_route = False

    def end_ride(self):
        self.client.put(f"/ride/{self.ride['id']}", headers=self.header)
