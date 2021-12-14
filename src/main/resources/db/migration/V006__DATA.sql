insert into questionnaire (name)
values ('Eating Habits'),
       ('Media Usage');

insert into question (questionnaire_id, text)
values (1, 'Do you eat Breakfast?'),
       (1, 'Breakfast is the most important meal of the day'),
       (1, 'How much do you like chocolate?'),
       (1, 'Comments?'),
       (2, 'Do you have a smartphone?'),
       (2, 'How many days a week do you watch TV'),
       (2, 'Do you have an iPad?'),
       (2, 'Comments?');

insert into alternative(alternative_text, question_id)
values ('Yes', 1),
       ('No', 1),
       ('Yes', 2),
       ('No', 2),
       ('1', 3),
       ('2', 3),
       ('3', 3),
       ('4', 3),
       ('Yes', 5),
       ('No', 5),
       ('1', 6),
       ('2', 6),
       ('3', 6),
       ('4', 6),
       ('5', 6),
       ('6', 6),
       ('7', 6),
       ('Yes', 7),
       ('No', 7);

insert into user_table(user_name)
values ('Roger');

insert into answer(answer_text, question_id, user_id)
values ('yes', 1, 1);