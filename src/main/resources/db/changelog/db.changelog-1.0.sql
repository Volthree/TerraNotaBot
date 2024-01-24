--liquibase formatted sql

--changeset vladislav:1
CREATE TABLE user_parameters
(
    id       serial PRIMARY KEY,
    updateid integer      NOT NULL,
    messageid  VARCHAR(50) NOT NULL,
    chatid bigint,
    mapsize integer,
    scale integer,
    heightDifference integer,
    islandsModifier integer,
    username varchar,
    date timestamp,
    mapid varchar
);