insert into role(name) values ('CLIENT');
insert into role(name) values ('DRIVER');
insert into role(name) values ('ADMIN');

insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Novi Sad', 0, 'sasalukic@gmail.com', 'Sasa', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '061345890', 'gadsgas', 'LOCAL', 'Lukic', 1);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Nis', 0, 'dejanmatic@gmail.com', 'Dejan', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '061389012', 'gadsgas', 'LOCAL', 'Matic', 3);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Beograd', 0, 'milicamatic@gmail.com', 'Milica', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '063454890', 'gadsgas', 'LOCAL', 'Matic', 1);
insert into my_user(account_status, active_account, blocked, city, driving_status, email, name, password, phone_number, profile_image, provider, surname, role_id) values (0, true, false, 'Kragujevac', 0, 'milospantic@gmail.com', 'Milos', '$2y$10$7eQE2MA0KNr/cSX9Inueeedn8BKki/8spU.nRidPgJCHT3FLt8ZGW', '064445512', 'gadsgas', 'LOCAL', 'Pantic', 3);

insert into message(sender_email, receiver_email, date, content, status) values('sasalukic@gmail.com', 'support', '2022-11-24 14:11:10', 'Hello, I have problem with scheduling drive.', 1);
insert into message(sender_email, receiver_email, date, content, status) values('support', 'sasalukic@gmail.com', '2022-11-24 14:17:10', 'What is your problem?.', 1);
insert into message(sender_email, receiver_email, date, content, status) values('sasalukic@gmail.com', 'support', '2022-11-24 14:30:10', 'My back is broken.', 1);