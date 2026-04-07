package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.ai.AiJob;
import com.ieltsascent.backend.domain.ai.AiJobStatus;
import com.ieltsascent.backend.domain.ai.AiJobType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiJobRepository extends JpaRepository<AiJob, Long> {
    Optional<AiJob> findByIdAndUserId(Long id, Long userId);

    @Query("""
        select job
        from AiJob job
        where job.status = :status
        order by job.createdAt asc
        """)
    List<AiJob> findByStatus(@Param("status") AiJobStatus status, Pageable pageable);

    @Query("""
        select count(job)
        from AiJob job
        where job.userId = :userId
          and job.type = :type
          and job.createdAt >= :since
        """)
    long countByUserIdAndTypeSince(
        @Param("userId") Long userId,
        @Param("type") AiJobType type,
        @Param("since") Instant since
    );
}
