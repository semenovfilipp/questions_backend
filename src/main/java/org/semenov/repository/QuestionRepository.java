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
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<QuestionDTO> findAll(Pageable pageable, @Param("user") User user);



    @Query("select new org.semenov.dto.QuestionDTO(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Question m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<QuestionDTO> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new org.semenov.dto.QuestionDTO(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Question m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<QuestionDTO> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
