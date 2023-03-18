CREATE TYPE account_type AS ENUM ('current', 'savings');

CREATE TABLE accounts
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR                 NOT NULL,
    owner_id    BIGINT REFERENCES users NOT NULL,
    currency_id VARCHAR(3)              NOT NULL REFERENCES currencies,
    type        account_type            NOT NULL,
    balance     BIGINT                  NOT NULL,
    UNIQUE (owner_id, name)
);