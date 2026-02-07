CREATE TABLE person
(
    id_person  SERIAL PRIMARY KEY,
    firstname  VARCHAR(50)  NOT NULL,
    lastname   VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    enabled    boolean      NOT NULL DEFAULT false,
    role       VARCHAR(30)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE img
(
    id_img    SERIAL PRIMARY KEY,
    path      VARCHAR(255),
    id_person INTEGER REFERENCES person (id_person) ON DELETE CASCADE
);