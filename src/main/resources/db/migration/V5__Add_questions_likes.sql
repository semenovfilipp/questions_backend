create table question_likes (
                                user_id bigint not null references usr,
                                question_id bigint not null references questions,
                                primary key (user_id, question_id)
);
