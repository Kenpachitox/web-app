CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username TEXT        NOT NULL UNIQUE,
    password TEXT        NOT NULL,
    active   BOOLEAN     NOT NULL DEFAULT TRUE,
    created  timestamptz NOT NULL DEFAULT current_timestamp
);
CREATE TABLE tokens
(
    token    TEXT PRIMARY KEY,
    "userId" BIGINT      NOT NULL REFERENCES users,
    created  timestamptz NOT NULL DEFAULT current_timestamp
);
CREATE TABLE registration_attempts
(
    id       BIGSERIAL PRIMARY KEY,
    username TEXT        NOT NULL UNIQUE,
    created  timestamptz NOT NULL DEFAULT current_timestamp
);
CREATE TABLE login_attempts
(
    id       BIGSERIAL PRIMARY KEY,
    username TEXT        NOT NULL UNIQUE,
    created  timestamptz NOT NULL DEFAULT current_timestamp
);
CREATE TABLE cards
(
    id        BIGSERIAL PRIMARY KEY,
    "ownerId" BIGINT  NOT NULL REFERENCES users,
    number    TEXT    NOT NULL,
    balance   BIGINT  NOT NULL DEFAULT 0,
    active    BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE roles
(
    id       BIGSERIAL PRIMARY KEY,
    role     TEXT   NOT NULL,
    "userId" BIGINT REFERENCES users,
    created  timestamptz NOT NULL DEFAULT current_timestamp
);

CREATE TABLE secret_codes
(
    code     TEXT        NOT NULL,
    "userId" BIGINT      NOT NULL REFERENCES users,
    created  timestamptz NOT NULL DEFAULT current_timestamp
);