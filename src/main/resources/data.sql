MERGE INTO friendship_status (name) KEY(name) VALUES ('PENDING_SENT');
MERGE INTO friendship_status (name) KEY(name) VALUES ('FRIENDS');
MERGE INTO friendship_status (name) KEY(name) VALUES ('DECLINED');

MERGE INTO genre (name) KEY(name) VALUES ('Комедия');
MERGE INTO genre (name) KEY(name) VALUES ('Драма');
MERGE INTO genre (name) KEY(name) VALUES ('Мультфильм');
MERGE INTO genre (name) KEY(name) VALUES ('Триллер');
MERGE INTO genre (name) KEY(name) VALUES ('Документальный');
MERGE INTO genre (name) KEY(name) VALUES ('Боевик');

MERGE INTO mpa (name) KEY(name) VALUES ('G');
MERGE INTO mpa (name) KEY(name) VALUES ('PG');
MERGE INTO mpa (name) KEY(name) VALUES ('PG-13');
MERGE INTO mpa (name) KEY(name) VALUES ('R');
MERGE INTO mpa (name) KEY(name) VALUES ('NC-17');