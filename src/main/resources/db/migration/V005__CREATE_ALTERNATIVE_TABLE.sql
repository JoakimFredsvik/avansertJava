CREATE TABLE alternative
(
    alternative_id   serial primary key,
    alternative_text varchar(500),
    question_id      int not null,
    CONSTRAINT fk_alternative_question
        FOREIGN KEY (question_id)
            REFERENCES question (question_id)
);