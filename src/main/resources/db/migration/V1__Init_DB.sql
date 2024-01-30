create sequence hibernate_sequence start 1 increment 1;

create table questions (
                           id bigint not null,
                           filename varchar(255),
                           tag varchar(255),
                           text varchar(2048) not null,
                           user_id bigint,
                           primary key (id)
);

create table user_role (
                           user_id bigint not null,
                           roles varchar(255)
);

create table usr (
                     id bigint not null,
                     activation_code varchar(255),
                     active boolean not null,
                     email varchar(255),
                     password varchar(255) not null,
                     username varchar(255) not null,
                     primary key (id)
);

alter table questions
    add constraint fk_questions_user
        foreign key (user_id) references usr;

alter table user_role
    add constraint fk_user_role_user
        foreign key (user_id) references usr;
