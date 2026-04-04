package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingContentStatus;
import com.ieltsascent.backend.domain.readingtest.ReadingDifficulty;
import com.ieltsascent.backend.domain.readingtest.ReadingTest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadingTestRepository extends JpaRepository<ReadingTest, UUID> {
    @Query("""
        select t from ReadingTest t
        where (:status is null or t.status = :status)
          and (:difficulty is null or t.difficulty = :difficulty)
          and (:premium is null or t.premium = :premium)
          and (:query is null or lower(t.title) like concat('%', :query, '%'))
        """)
    Page<ReadingTest> searchAdmin(@Param("status") ReadingContentStatus status,
                                  @Param("difficulty") ReadingDifficulty difficulty,
                                  @Param("premium") Boolean premium,
                                  @Param("query") String query,
                                  Pageable pageable);

    @Query("""
        select t from ReadingTest t
        where t.status = com.ieltsascent.backend.domain.readingtest.ReadingContentStatus.PUBLISHED
          and (:premiumUser = true or t.premium = false)
        order by t.updatedAt desc
        """)
    List<ReadingTest> findAvailable(@Param("premiumUser") boolean premiumUser);
}
