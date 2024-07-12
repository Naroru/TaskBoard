

CREATE TABLE IF NOT EXISTS users
(
    id       bigserial primary key,
    name     varchar(255) not null,
    username varchar(255) not null unique,
    password varchar(255) not null

);


CREATE TABLE IF NOT EXISTS tasks
(
    id              bigserial primary key,
    title           varchar(255)                                                       not null,
    description     text,
    status          varchar(255)                                                       not null,
    expiration_date timestamp,
    fk_user_id      bigint REFERENCES users (id) on delete cascade on update no action not null
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint       not null,
    role    varchar(255) not null,
    primary key (user_id, role),
    CONSTRAINT fk_users_roles_user_id FOREIGN KEY (user_id) references users (id) on delete cascade on update no action
);


