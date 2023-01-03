import requests
import json

from locust import HttpUser, task, between, events, run_single_user
from random import randrange


start_and_end_points = [
    (45.235866, 19.807387),     # Djordja Mikeša 2
    (45.247309, 19.796717),     # Andje Rankovic 2
    (45.259711, 19.809787),     # Veselina Maslese 62
    (45.261421, 19.823026),     # Jovana Hranilovica 2
    (45.265435, 19.847805),     # Bele njive 24
    (45.255521, 19.845071),     # Njegoseva 2
    (45.249241, 19.852152),     # Stevana Musica 20
    (45.242509, 19.844632),     # Boska Buhe 10A
    (45.254366, 19.861088),     # Strosmajerova 2
    (45.223481, 19.847990)      # Gajeva 2
]


taxi_stops = [
    (45.238548, 19.848225),   # Stajaliste na keju
    (45.243097, 19.836284),   # Stajaliste kod limanske pijace
    (45.256863, 19.844129),   # Stajaliste kod trifkovicevog trga
    (45.255055, 19.810161),   # Stajaliste na telepu
    (45.246540, 19.849282)    # Stajaliste kod velike menze
]


license_plates = [
    'NS-001-AA',
    'NS-001-AB',
    'NS-001-AC'
]


#
# @events.test_start.add_listener
# def on_test_start(environment, **kwargs):
#     requests.delete('http://localhost:8081/api/ride')
#     requests.delete('http://localhost:8081/api/vehicle')


class QuickstartUser(HttpUser):
    host = 'http://localhost:8081/api'
    wait_time = between(0.5, 2)
    taken_ride_ids = []

    def on_start(self):
        headers = {'Content-Type': 'application/json; charset=utf-8'}
        response = self.client.post("/auth/login", json={"email": "sasalukic@gmail.com", "password": "sasa123"}, headers=headers)
        accessToken = json.loads(response.text)["accessToken"]
        self.header = {'Authorization': 'Bearer '+accessToken}

        self.rides = self.client.get('/rides/active-rides', headers=self.header).json()
        print(self.rides)
        self.points = self.ride.routePoints
        self.pointIndex = 0

        self.driver = self.ride.driver

        self.driving_to_start_point = True
        self.driving_the_route = False
        self.stopped = False

        self.departure = (self.driver.latitude, self.driver.longitude)
        self.destination = (self.points[0].latitude, self.points[0].longitude)
        self.get_new_coordinates()

    @task
    def update_vehicle_coordinates(self):
        if len(self.coordinates) > 0:
            new_coordinate = self.coordinates.pop(0)
            self.client.put(f"/api/map/{self.ride.rideId}", json={
                'latitude': new_coordinate[0],
                'longitude': new_coordinate[1]
            })
        elif len(self.coordinates) == 0 and self.driving_to_start_point:
            self.departure = self.destination
            self.pointIndex += 1
            self.destination = (self.points[self.pointIndex].latitude, self.points[self.pointIndex].longitude)
            self.get_new_coordinates()
            self.driving_to_start_point = False
            self.driving_the_route = True
        elif len(self.coordinates) == 0 and self.driving_the_route:
            self.end_ride()
            self.driving_the_route = False
            self.stopped = True
        elif len(self.coordinates) == 0 and self.stopped:
            self.ride = self.client.get('/api/rides/active-rides').json()
            if self.ride:
                self.driving_to_start_point = True
                self.stopped = False



    def get_new_coordinates(self):
        response = requests.get(f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{self.departure[1]},{self.departure[0]};{self.destination[1]},{self.destination[0]}?geometries=geojson&overview=false&alternatives=true&steps=true')
        response = requests.get("/determine-optimal-route", )

        self.routeGeoJSON = response.json()
        self.coordinates = []
        for step in self.routeGeoJSON['routes'][0]['legs'][0]['steps']:
            self.coordinates = [*self.coordinates, *step['geometry']['coordinates']]
        # self.ride = self.client.post('/api/ride', json={
        #     'routeJSON': json.dumps(self.routeGeoJSON),
        #     'rideStatus': 0,
        #     'vehicle': {
        #         'id': self.vehicle['id'],
        #         'licensePlateNumber': self.vehicle['licensePlateNumber'],
        #         'latitude': self.coordinates[0][0],
        #         'longitude': self.coordinates[0][1]
        #     }
        # }).json()

    def end_ride(self):
        self.client.put(f"/api/ride/{self.ride.rideId}")

