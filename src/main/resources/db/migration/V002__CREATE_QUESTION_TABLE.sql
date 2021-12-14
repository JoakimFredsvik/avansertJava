create table question
(
    question_id      serial primary key,
    questionnaire_id int          not null,
    text             varchar(300) not null,
    deleted boolean default false,
    CONSTRAINT fk_questionnaire
        FOREIGN KEY (questionnaire_id)
            REFERENCES questionnaire (questionnaire_id)
);