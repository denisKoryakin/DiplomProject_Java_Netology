create schema if not exists netology;

CREATE SEQUENCE if not exists netology.users_seq START 1 INCREMENT 50;
CREATE SEQUENCE if not exists netology.files_seq START 1 INCREMENT 50;

create table netology.users
(
    userid    bigint not null constraint users_pkey primary key,
    password  varchar(255)  not null,
    roles     varchar(255)  not null,
    user_name varchar(255)  not null constraint uk_k8d0f2n7n88w1a16yhua64onx unique
);

alter table netology.users
    owner to postgres;

create table netology.files
(
    id          bigint              not null constraint files_pkey primary key,
    file_name   varchar(255)        not null constraint uk_7ri9c1356mu0q6qek5krlrrg8 unique,
    size        double precision    not null,
    user_userid bigint              not null constraint fkigsm2mejqe3fa8tk7wg8sppfr references netology.users
);

alter table netology.files
    owner to postgres;