package org.semenov.service;

import org.semenov.dto.QuestionDTO;
import org.semenov.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

    Page<QuestionDTO> questionList(Pageable pageable, String filter, User user);
    Page<QuestionDTO> questionListForUser(Pageable pageable, User currentUser, User author);

}
