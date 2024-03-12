create table "user"
(
    id       BIGINT generated always as identity,
    name     varchar(50) not null,
    surname  varchar(50) not null,
    username varchar(50) not null,
    constraint pk_user primary key (id)
);

create table authentication
(
    id          bigint generated always as identity,
    password    varchar  not null,
    retry_count smallint not null,
    locked      boolean  not null,
    user_id     bigint   not null
        constraint authentication_user_id_fk
            references "user" (id),
    constraint pk_authentication primary key (id)
);

create table session
(
    id                bigint generated always as identity
        constraint session_pk
            primary key,
    timestamp         timestamp without time zone not null,
    authentication_id bigint   not null
        constraint session_authentication_id_fk
            references authentication (id)
);