create table articles (
    id      serial not null primary key,
    title   text       null default null,
    content text       null default null
);
