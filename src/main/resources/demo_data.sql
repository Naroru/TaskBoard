INSERT INTO users (name, username, password)
VALUES ('John Smith', 'Killer', '$2a$10$YHw59j6poUcWvhcNhSB3WeKp6KcgIINpFhDOScrRqvg6v2szDhGx2'),
       ('Mark Roby', 'Noob', '$2a$10$YHw59j6poUcWvhcNhSB3WeKp6KcgIINpFhDOScrRqvg6v2szDhGx2'
);

INSERT INTO tasks(title, description, status, expiration_date, fk_user_id)
VALUES ('Call to supplier', null, 'IN_PROGRESS', '2024-06-23 00:00:00', 1),
       ('Kill enemy', 'Make a feature in a game to kill enemy', 'TODO', '2024-07-23 00:00:00', 2),
       ('Make a cofee', 'John, make me coffe, please', 'TODO', '2024-07-23 15:00:00', 1);

INSERT INTO users_roles
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_ADMIN'),
       (2, 'ROLE_USER')

