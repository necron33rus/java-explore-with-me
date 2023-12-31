DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app       VARCHAR(100),
    uri       VARCHAR(512),
    ip        VARCHAR(50),
    timestamp TIMESTAMP WITHOUT TIME ZONE
);