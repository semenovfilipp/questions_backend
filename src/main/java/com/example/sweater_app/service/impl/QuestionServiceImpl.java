package com.example.sweater_app.service.impl;

import com.example.sweater_app.domain.Question;
import com.example.sweater_app.dto.QuestionDTO;
import com.example.sweater_app.mapper.QuestionDtoMapper;
import com.example.sweater_app.repository.QuestionRepository;
import com.example.sweater_app.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionDtoMapper questionDtoMapper;

    @Override
    public Question addQuestion(String title, String description, String tag) {
        Question question = Question.builder()
                .title(title)
                .description(description)
                .tag(tag)
                .build();
        return questionRepository.save(question);
    }

    @Override
    public List<QuestionDTO> getAllQuestionsDTO() {
        Iterable<Question> questions = questionRepository.findAll();

        return StreamSupport.stream(questions.spliterator(), false)
                .map(questionDtoMapper::toQuestionDto)
                .collect(Collectors.toList());
    }
}

