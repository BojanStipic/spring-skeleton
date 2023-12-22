create type user_role as enum ('USER', 'ADMIN');

create table application_user (
    id bigint primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    name varchar(255),
    last_name varchar(255),
    role user_role not null,
    created_at timestamp,
    created_by varchar(255),
    updated_at timestamp,
    updated_by varchar(255)
);

create sequence application_user_id_seq start 1 increment 50;
