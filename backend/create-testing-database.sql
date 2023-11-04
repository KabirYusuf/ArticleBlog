create table authors (
                         id          serial not null primary key,
                         name    text        null default null
);

create table articles (
    id      serial not null primary key,
    title   text       null default null,
    content text       null default null,
    author_id integer not null references authors
);

create table comments (
    id          serial not null primary key,
    article_id integer not null references articles,
    content    text        null default null,
    author_id integer not null references authors
);


