create table task
(
    id         uuid                        not null,
    created_at timestamp(6) with time zone not null,
    updated_at timestamp(6) with time zone not null,
    version    integer                     not null,
    done       boolean                     not null,
    title      varchar(255)                not null,
    constraint pk_task__id primary key (id)
);