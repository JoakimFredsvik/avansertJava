
create table answer
(
    answer_text text,
    question_id  int not null,
    user_id      int not null,
    answer_id serial primary key ,
    CONSTRAINT fk_question
        FOREIGN KEY (question_id)
            REFERENCES question (question_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES user_table (user_id)
);