insert into role(name) values ('CLIENT');
insert into role(name) values ('DRIVER');
insert into role(name) values ('ADMIN');

insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Novi Sad', 1, 'sasalukic@gmail.com', 'Sasa', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '061345890', '/sasaProfile.jpg', 'LOCAL', 'Lukic', 1);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Nis', 1, 'dejanmatic@gmail.com', 'Dejan', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '061389012', '/dejanProfile.jpg', 'LOCAL', 'Matic', 2);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Beograd', 1, 'milicamatic@gmail.com', 'Milica', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '063454890', 'https://buffer.com/library/content/images/2022/03/amina.png', 'LOCAL', 'Matic', 1);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Kragujevac', 1, 'marinapantic@gmail.com', 'Marina', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '064445512', 'https://d2qp0siotla746.cloudfront.net/img/use-cases/profile-picture/template_3.jpg', 'LOCAL', 'Pantic', 3);

insert into message(sender_email, receiver_email, date, content, status) values('sasalukic@gmail.com', 'support', '2022-11-24 14:11:10', 'Hello, I have problem with scheduling drive.', 1);
insert into message(sender_email, receiver_email, date, content, status) values('support', 'sasalukic@gmail.com', '2022-11-24 14:17:10', 'What is your problem?.', 1);
insert into message(sender_email, receiver_email, date, content, status) values('sasalukic@gmail.com', 'support', '2022-11-24 14:30:10', 'My back is broken.', 1);

insert into client(id, tokens) values(1, 10);

insert into vehicle_type(coefficient, type) values (1, 'Standard');
insert into vehicle_type(coefficient, type) values (1.2, 'Baby Seat');
insert into vehicle_type(coefficient, type) values (1.3, 'Pet Friendly');
insert into vehicle_type(coefficient, type) values (1.5, 'Baby Seat and Pet Friendly');

insert into ride(id) values(1);
insert into ride(id) values(2);
insert into ride(id) values(3);
insert into ride(id) values(4);
insert into ride(id) values(5);
insert into ride(id) values(6);

insert into ride_invite(email_from, email_to, price_to_pay, ride_invite_status, first_location, destination) values('dejanmatic@gmail.com', 'sasalukic@gmail.com', 5.3, 2, 'Bulevar Vojvode Stepe 125', 'Bulevar Evrope Novi Sad')

