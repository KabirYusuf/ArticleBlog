create table users (
                         id          serial not null primary key,
                         first_name    text        null default null,
                         last_name    text        null default null,
                         email        text        null default null
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


