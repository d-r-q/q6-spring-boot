CREATE TABLE users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    VARCHAR UNIQUE NOT NULL,
    password VARCHAR(60)    NOT NULL,
    name     VARCHAR        NOT NULL,
    roles    VARCHAR[]
);