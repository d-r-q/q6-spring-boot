
CREATE TABLE groups
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owner_id BIGINT REFERENCES users,
    name     VARCHAR NOT NULL,
    UNIQUE (owner_id, name)
);

CREATE TABLE user_groups
(
    user_id  BIGINT REFERENCES users,
    group_id BIGINT REFERENCES groups,
    PRIMARY KEY (user_id, group_id)
);
