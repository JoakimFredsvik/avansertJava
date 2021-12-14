create table questionnaire
(
    questionnaire_id serial primary key,
    name             varchar(200) not null,
    deleted boolean default false
);





