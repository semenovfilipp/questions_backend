package org.semenov.dto;

import lombok.*;
import org.semenov.model.User;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
        private Long id;

        private String text;

        private String tag;

        private User author;

        private String filename;

        private Long likes;

        private Boolean meLiked;


}
