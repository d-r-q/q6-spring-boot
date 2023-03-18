create type category_type as enum ('income', 'expense');

CREATE TABLE category_groups
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name      VARCHAR                 NOT NULL,
    owner_id  BIGINT REFERENCES users NOT NULL,
    type      category_type           NOT NULL,
    parent_id BIGINT REFERENCES category_groups,
    UNIQUE (owner_id, name)
);

CREATE TABLE categories
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR                           NOT NULL,
    category_group_id BIGINT REFERENCES category_groups NOT NULL,
    UNIQUE (category_group_id, name)
);