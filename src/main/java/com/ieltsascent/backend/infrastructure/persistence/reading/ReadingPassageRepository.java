package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.enums.ReadingContentStatus;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingDifficulty;
import com.ieltsascent.backend.domain.readingtest.ReadingPassage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingPassageRepository extends JpaRepository<ReadingPassage, Long> {
    @Query("""
        select p from ReadingPassage p
        where (:status is null or p.status = :status)
          and (:difficulty is null or p.difficulty = :difficulty)
          and (:premium is null or p.premium = :premium)
          and (:topicTag is null or lower(p.topicTag) = lower(:topicTag))
          and (:query is null or lower(p.title) like concat('%', :query, '%') or lower(coalesce(p.shortDescription,'')) like concat('%', :query, '%'))
        """)
    Page<ReadingPassage> searchAdmin(@Param("status") ReadingContentStatus status,
                                     @Param("difficulty") ReadingDifficulty difficulty,
                                     @Param("premium") Boolean premium,
                                     @Param("topicTag") String topicTag,
                                     @Param("query") String query,
                                     Pageable pageable);

    @Query("""
        select p from ReadingPassage p
        where p.status = ReadingContentStatus.PUBLISHED
          and (:premiumUser = true or p.premium = false)
          and p.ieltsBandMin <= :targetBand and p.ieltsBandMax >= :currentBand
        """)
    List<ReadingPassage> findEligibleForUser(@Param("premiumUser") boolean premiumUser,
                                             @Param("currentBand") double currentBand,
                                             @Param("targetBand") double targetBand);
}
