INSERT INTO user (id, email, password, first_name, last_name)
VALUES (1, 'email@i.ua', 'password', 'Denis', 'Unknown');

INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER');

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);