--liquibase formatted sql

--changeset vladislav:1
ALTER TABLE user_parameters
    alter column mapid type varchar using mapid::jsonb;
ALTER TABLE user_parameters
    ADD maphash int;