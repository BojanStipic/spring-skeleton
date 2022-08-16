create type user_role as enum ('USER', 'ADMIN');

create table application_user (
    id bigint primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    name varchar(255),
    last_name varchar(255),
    role user_role not null
);

create sequence application_user_id_seq start 1 increment 50;
