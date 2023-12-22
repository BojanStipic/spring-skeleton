drop sequence application_user_id_seq;
drop table application_user;
drop type user_role;

delete from flyway_schema_history where script = 'V1__users.sql';
