package org.semenov.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.semenov.utils.QuestionHelper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "filename")
    private String fileName;

    @NotBlank(message = "Please fill the question")
    @Length(max = 2048, message = "Question is too long (more than 2kB)")
    @Column(name = "text")
    private String text;

    @Length(max = 255, message = "Tag is too long (more than 255)")
    @Column(name = "tag")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Column(name = "author")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "question_likes",
            joinColumns = { @JoinColumn(name = "question_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public String getAuthorName() {
        return QuestionHelper.getAuthorName(author);
    }
}

