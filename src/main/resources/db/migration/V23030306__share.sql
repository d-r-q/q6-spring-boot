CREATE TYPE share_mode AS ENUM ('ro', 'rw');

CREATE TABLE account_shares
(
    account_id BIGINT REFERENCES accounts,
    group_id   BIGINT REFERENCES groups,
    mode       share_mode NOT NULL,
    PRIMARY KEY (account_id, group_id)
);

CREATE TABLE category_group_shares
(
    category_group_id BIGINT REFERENCES category_groups,
    group_id          BIGINT REFERENCES groups,
    mode              share_mode NOT NULL,
    PRIMARY KEY (category_group_id, group_id)
);
