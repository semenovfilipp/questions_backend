package org.semenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.semenov.dto.QuestionDTO;
import org.semenov.model.User;
import org.semenov.repository.QuestionRepository;
import org.semenov.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Page<QuestionDTO> questionList(Pageable pageable, String filter, User user) {
        try {
            if (filter != null && !filter.isEmpty()) {
                return questionRepository.findByTag(filter, pageable, user);
            } else {
                return questionRepository.findAll(pageable, user);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching question list: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<QuestionDTO> questionListForUser(Pageable pageable, User currentUser, User author) {
        try {
            return questionRepository.findByUser(pageable, author, currentUser);
        } catch (Exception e) {
            log.error("Error occurred while fetching question list for user: {}", e.getMessage());
            throw e;
        }
    }
}
