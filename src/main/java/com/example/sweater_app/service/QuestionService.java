package com.example.sweater_app.service;

import com.example.sweater_app.domain.Question;
import com.example.sweater_app.dto.QuestionDTO;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    Question addQuestion(String title, String description, String tag);
    List<QuestionDTO> getAllQuestionsDTO();
}
