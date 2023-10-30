package com.example.sweater_app.repository;

import com.example.sweater_app.domain.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findQuestionByTag(String tag);
}
