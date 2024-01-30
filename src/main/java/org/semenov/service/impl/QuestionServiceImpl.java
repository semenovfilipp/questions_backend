package org.semenov.service.impl;

import lombok.RequiredArgsConstructor;
import org.semenov.dto.QuestionDTO;
import org.semenov.model.User;
import org.semenov.repository.QuestionRepository;
import org.semenov.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;


    @Override
    public Page<QuestionDTO> questionList(Pageable pageable, String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
            return questionRepository.findByTag(filter, pageable, user);
        } else {
            return questionRepository.findAll(pageable, user);
        }
    }

    @Override
    public Page<QuestionDTO> questionListForUser(Pageable pageable, User currentUser, User author) {
        return questionRepository.findByUser(pageable, author, currentUser);
    }
}

