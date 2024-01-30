package org.semenov.repository;

import org.semenov.dto.QuestionDTO;
import org.semenov.model.Question;
import org.semenov.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select new org.semenov.dto.QuestionDTO(" +
            "   m.id, " +
            "   m.text, " +
            "   m.tag, " +
            "   m.author, " +
            "   m.fileName, " +
            "   count(ml), " +
            "   case when :user member of m.likes then true else false end" +
            ") " +
            "from Question m left join m.likes ml " +
            "group by m.id, m.text, m.tag, m.author, m.fileName")
    Page<QuestionDTO> findAll(Pageable pageable, @Param("user") User user);




    @Query("select new org.semenov.dto.QuestionDTO(" +
            "   m.id, " +
            "   m.text, " +
            "   m.tag, " +
            "   m.author, " +
            "   m.fileName, " +
            "   count(ml), " +
            "   case when :user member of m.likes then true else false end" +
            ") " +
            "from Question m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m.id, m.text, m.tag, m.author, m.fileName")
    Page<QuestionDTO> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new org.semenov.dto.QuestionDTO(" +
            "   m.id, " +
            "   m.text, " +
            "   m.tag, " +
            "   m.author, " +
            "   m.fileName, " +
            "   count(ml), " +
            "   case when :user member of m.likes then true else false end" +
            ") " +
            "from Question m left join m.likes ml " +
            "where m.author = :author " +
            "group by m.id, m.text, m.tag, m.author, m.fileName")
    Page<QuestionDTO> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);



}
