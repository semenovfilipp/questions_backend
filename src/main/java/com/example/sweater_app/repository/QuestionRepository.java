package com.example.sweater_app.repository;

import com.example.sweater_app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findQuestionByTag(String tag);
}
