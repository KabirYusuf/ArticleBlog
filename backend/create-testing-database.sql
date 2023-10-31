create table articles (
    id      serial not null primary key,
    title   text       null default null,
    content text       null default null
);

create table comments (
    id          serial not null primary key,
    article_id integer not null references articles,
    content    text        null default null
);
