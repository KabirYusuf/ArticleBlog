CREATE TABLE users (
                       id SERIAL NOT NULL PRIMARY KEY,
                       first_name TEXT NULL DEFAULT NULL,
                       last_name TEXT NULL DEFAULT NULL,
                       email TEXT NOT NULL DEFAULT NULL UNIQUE,
                       password TEXT NOT NULL DEFAULT NULL,
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       is_verified BOOLEAN DEFAULT FALSE
);
CREATE INDEX index_email ON users (email);


CREATE TABLE user_roles (
                            user_id INTEGER NOT NULL REFERENCES users(id),
                            role_name TEXT NOT NULL,
                            PRIMARY KEY (user_id, role_name)
);


create table articles (
    id      serial not null primary key,
    title   text       null default null,
    content text       null default null,
    user_id integer not null references users
);

create table comments (
    id          serial not null primary key,
    article_id integer not null references articles,
    content    text        null default null,
    user_id integer not null references users
);

CREATE TABLE tokens (
                        id SERIAL NOT NULL PRIMARY KEY,
                        hashed_token TEXT NOT NULL,
                        user_id INTEGER NOT NULL,
                        is_revoked BOOLEAN DEFAULT FALSE
);
CREATE INDEX index_hashed_token ON tokens (hashed_token);




