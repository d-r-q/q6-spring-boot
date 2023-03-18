CREATE TYPE actual_transaction_type AS ENUM ('INTERNAL', 'EXTERNAL');

CREATE TABLE actual_transactions
(
    id            BIGINT,
    created_by_id BIGINT REFERENCES users NOT NULL,
    amount        BIGINT                  NOT NULL,
    date_time     TIMESTAMPTZ             NOT NULL,
    comment       varchar                 NOT NULL,
    tags          varchar[]               NOT NULL,
    type          actual_transaction_type NOT NULL
);

CREATE TABLE external_transactions
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type        actual_transaction_type      NOT NULL CHECK (type = 'EXTERNAL'),
    account_id  BIGINT REFERENCES accounts   NOT NULL,
    category_id BIGINT REFERENCES categories NOT NULL
) INHERITS (actual_transactions);


CREATE OR REPLACE TRIGGER on_external_transaction_insert_trigger
    AFTER INSERT OR DELETE OR UPDATE OF amount
    ON external_transactions
    FOR EACH ROW
EXECUTE PROCEDURE on_external_transaction_change();

CREATE TABLE internal_transactions
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type              actual_transaction_type    NOT NULL CHECK (type = 'INTERNAL'),
    source_account_id BIGINT REFERENCES accounts NOT NULL,
    target_account_id BIGINT REFERENCES accounts NOT NULL,
    rate              BIGINT
) INHERITS (actual_transactions);

CREATE OR REPLACE TRIGGER on_internal_transaction_insert_trigger
    AFTER INSERT OR DELETE OR UPDATE OF amount
    ON internal_transactions
    FOR EACH ROW
EXECUTE PROCEDURE on_internal_transaction_change();

CREATE TABLE planned_transactions
(
    id                BIGINT,
    created_by_id     BIGINT REFERENCES users      NOT NULL,
    source_account_id BIGINT REFERENCES accounts   NOT NULL,
    amount            BIGINT                       NOT NULL,
    date_time         TIMESTAMPTZ                  NOT NULL,
    comment           varchar                      NOT NULL,
    tags              varchar[]                    NOT NULL,
    duration          INTERVAL,
    category_id       BIGINT REFERENCES categories NOT NULL
);
