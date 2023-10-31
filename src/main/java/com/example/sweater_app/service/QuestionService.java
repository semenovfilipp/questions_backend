package com.example.sweater_app.service;

import com.example.sweater_app.model.Question;
import com.example.sweater_app.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {
    Question addQuestion(String title, String description, String tag);
    List<QuestionDTO> getAllQuestionsDTO();
    List<QuestionDTO> findQuestionsByTag(String tag);
}
