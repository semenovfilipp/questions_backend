package com.example.sweater_app.mapper;

import com.example.sweater_app.domain.Question;
import com.example.sweater_app.dto.QuestionDTO;
import org.springframework.stereotype.Component;



@Component
public class QuestionDtoMapper {

    public QuestionDTO toQuestionDto(Question question){
        return QuestionDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .tag(question.getTag())
                .build();
    }
}
