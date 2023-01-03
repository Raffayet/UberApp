insert into role(name) values ('CLIENT');
insert into role(name) values ('DRIVER');
insert into role(name) values ('ADMIN');

insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Novi Sad', 1, 'sasalukic@gmail.com', 'Sasa', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '061345890', '/sasaProfile.jpg', 'LOCAL', 'Lukic', 1);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Nis', 1, 'dejanmatic@gmail.com', 'Dejan', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '061389012', '/dejanProfile.jpg', 'LOCAL', 'Matic', 2);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Beograd', 1, 'milicamatic@gmail.com', 'Milica', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '063454890', 'https://buffer.com/library/content/images/2022/03/amina.png', 'LOCAL', 'Matic', 1);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Kragujevac', 1, 'marinapantic@gmail.com', 'Marina', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '064445512', 'https://d2qp0siotla746.cloudfront.net/img/use-cases/profile-picture/template_3.jpg', 'LOCAL', 'Pantic', 3);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Beograd', 1, 'strahinjapavlovic@gmail.com', 'Strahinja', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '063154890', 'https://buffer.com/library/content/images/2022/03/amina.png', 'LOCAL', 'Pavlovic', 1);

insert into message(sender_email, receiver_email, date, content, status) values('sasalukic@gmail.com', 'support', '2022-11-24 14:11:10', 'Hello, I have problem with scheduling drive.', 1);
insert into message(sender_email, receiver_email, date, content, status) values('support', 'sasalukic@gmail.com', '2022-11-24 14:17:10', 'What is your problem?.', 1);
insert into message(sender_email, receiver_email, date, content, status) values('sasalukic@gmail.com', 'support', '2022-11-24 14:30:10', 'My back is broken.', 1);

insert into point(lat, lng) values(45.34, 19.78);

insert into client(id, tokens) values(1, 10);
insert into client(id, tokens) values(3, 10);
insert into client(id, tokens) values(5, 10);
insert into driver(id, current_location_id, daily_active_interval) values(2, 1, 350.0);

-- insert into vehicle_type(type, coefficent) values('Regular', 2.3);

insert into map_search_result_dto(display_name, lon, lat) values ('Bulevar Cara Lazala', '45.26', '19.83');
insert into map_search_result_dto(display_name, lon, lat) values ('Bulevar Cara Dusana', '45.27', '19.81');

insert into ride(driver_id, initiator_id, price, price_per_passenger, vehicle_type, route_type) values(2, 1, 6.78, 2, 'Regular', 'Custom');

insert into ride_clients(ride_id, clients_id) values (1, 1);
insert into ride_clients(ride_id, clients_id) values (1, 3);

insert into ride_locations(ride_id, locations_id) values (1, 1);
insert into ride_locations(ride_id, locations_id) values (1, 2);

insert into driver_rides(driver_id, rides_id) values (2, 1);
-- insert into ride(id) values(2);
-- insert into ride(id) values(3);
-- insert into ride(id) values(4);
-- insert into ride(id) values(5);
-- insert into ride(id) values(6);

insert into ride_invite(email_from, email_to, price_to_pay, ride_invite_status, first_location, destination) values('dejanmatic@gmail.com', 'sasalukic@gmail.com', 5.3, 2, 'Bulevar Vojvode Stepe 125', 'Bulevar Evrope Novi Sad')

