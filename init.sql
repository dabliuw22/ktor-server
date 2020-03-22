CREATE TABLE emojis (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    phrase VARCHAR(255)
);

CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255)
);