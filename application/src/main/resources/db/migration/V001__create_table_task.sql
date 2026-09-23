create table task
(
    id         bigint generated always as identity not null,
    key        uuid                                not null,
    created_at timestamp(6) with time zone         not null,
    updated_at timestamp(6) with time zone         not null,
    version    bigint                              not null,
    title      varchar(255)                        not null,
    done       boolean                             not null,
    constraint pk_task__id primary key (id),
    constraint uk_task__title unique (title)
);