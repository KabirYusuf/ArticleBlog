CREATE TABLE users (
                       id SERIAL NOT NULL PRIMARY KEY,
                       first_name TEXT NULL DEFAULT NULL,
                       last_name TEXT NULL DEFAULT NULL,
                       email TEXT NULL DEFAULT NULL,
                       username TEXT NOT NULL DEFAULT NULL UNIQUE,
                       password TEXT NOT NULL DEFAULT NULL,
                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       is_verified BOOLEAN DEFAULT FALSE,
                       user_image TEXT NULL DEFAULT NULL
);



CREATE TABLE user_roles (
                            user_id INTEGER NOT NULL REFERENCES users(id),
                            role_name TEXT NOT NULL,
                            PRIMARY KEY (user_id, role_name)
);


create table articles (
                          id      serial not null primary key,
                          title   text       null default null,
                          content text       null default null,
                          user_id integer not null references users,
                          article_image TEXT NULL DEFAULT NULL,
                          created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

create table comments (
                          id          serial not null primary key,
                          article_id integer not null references articles,
                          content    text        null default null,
                          user_id integer not null references users,
                          created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE verification_tokens (
                                     id SERIAL NOT NULL PRIMARY KEY,
                                     token TEXT NOT NULL UNIQUE ,
                                     user_id INTEGER NOT NULL,
                                     expired_at TIMESTAMP WITHOUT TIME ZONE
);
