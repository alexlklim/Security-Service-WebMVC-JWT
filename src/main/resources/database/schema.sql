
CREATE TABLE IF NOT EXISTS users
(
    id            BIGINT                NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created       DATETIME,
    updated       DATETIME,
    last_activity DATETIME              NOT NULL,
    is_active     BOOLEAN               NOT NULL,
    firstname     VARCHAR(255)          NOT NULL,
    lastname      VARCHAR(255)          NOT NULL,

    email         VARCHAR(255) UNIQUE   NOT NULL,
    password      VARCHAR(255)          NOT NULL,
    roles         ENUM ('ADMIN', 'USER') NOT NULL
);



CREATE TABLE IF NOT EXISTS token
(
    id      BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token   BINARY(16),
    created DATETIME,
    expired DATETIME,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);