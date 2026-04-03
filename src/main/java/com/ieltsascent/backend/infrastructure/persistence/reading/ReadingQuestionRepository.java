package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingContentStatus;
import com.ieltsascent.backend.domain.readingtest.ReadingQuestion;
import com.ieltsascent.backend.domain.readingtest.ReadingQuestionKind;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingQuestionRepository extends JpaRepository<ReadingQuestion, UUID> {
    @Query("""
        select q from ReadingQuestion q
        where (:status is null or q.status = :status)
          and (:type is null or q.type = :type)
          and (:passageId is null or q.passage.id = :passageId)
          and (:premium is null or q.premium = :premium)
          and (:query is null or lower(q.prompt) like concat('%', :query, '%'))
        """)
    Page<ReadingQuestion> searchAdmin(@Param("status") ReadingContentStatus status,
                                      @Param("type") ReadingQuestionKind type,
                                      @Param("passageId") UUID passageId,
                                      @Param("premium") Boolean premium,
                                      @Param("query") String query,
                                      Pageable pageable);

    List<ReadingQuestion> findByPassageIdAndStatusOrderByOrderIndexAsc(UUID passageId, ReadingContentStatus status);

    @Query("""
        select q from ReadingQuestion q
        where q.status = com.ieltsascent.backend.domain.readingtest.ReadingContentStatus.PUBLISHED
          and q.passage.id = :passageId
          and (:premiumUser = true or (q.premium = false and q.passage.premium = false))
        order by q.orderIndex asc
        """)
    List<ReadingQuestion> findPracticeByPassage(@Param("passageId") UUID passageId, @Param("premiumUser") boolean premiumUser);

    @Query("""
        select q from ReadingQuestion q
        where q.status = com.ieltsascent.backend.domain.readingtest.ReadingContentStatus.PUBLISHED
          and q.type = :type
          and (:premiumUser = true or q.premium = false)
        order by q.orderIndex asc
        """)
    List<ReadingQuestion> findPracticeByType(@Param("type") ReadingQuestionKind type, @Param("premiumUser") boolean premiumUser);
}
