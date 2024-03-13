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

create table tmsch.currency
(
    id   smallint generated always as identity
        constraint currency_pk
            primary key,
    name varchar(20) not null,
    code varchar(3)  not null
);

create table tmsch.exchange_rate
(
    id            smallint generated always as identity
        constraint exchange_rate_pk
            primary key,
    currency_id   smallint not null
        constraint exchange_rate_currency_id_fk
            references tmsch.currency,
    exchange_rate numeric  not null
);

create table tmsch.account
(
    id             bigint generated always as identity
        constraint account_pk
            primary key,
    balance        numeric  not null,
    currency_id    smallint not null
        constraint account_currency_id_fk
            references tmsch."user",
    account_number uuid     not null,
    user_id        integer  not null
        constraint account_uk_2
            unique
);

alter table tmsch.account
    add constraint account_user_id_fk
        foreign key (user_id) references tmsch."user";

create table tmsch.transaction_type
(
    id   smallint generated always as identity
        constraint transaction_type_pk
            primary key,
    name varchar(20) not null,
    code varchar(10) not null
);

create table tmsch.transaction
(
    id                  bigint generated always as identity
        constraint transaction_pk
            primary key,
    transaction_type_id smallint
        constraint transaction_type_id_fk
            references tmsch.transaction_type (id),
    user_id             bigint   not null
        constraint transaction_user_id_fk
            references tmsch."user" (id),
    debit_account       bigint   not null
        constraint transaction_debit_account_id_fk
            references tmsch.account (id),
    credit_account      bigint   not null
        constraint transaction_credit_account_id_fk
            references tmsch.account (id),
    control_sum         numeric  not null,
    currency_id         smallint not null
        constraint transaction_currency_id_fk
            references tmsch.currency (id),
    session_id          bigint   not null
        constraint transaction_session_id_fk
            references tmsch.session (id)
);

alter table tmsch.authentication
    rename column password to username;

alter table tmsch."user"
    drop column username;
