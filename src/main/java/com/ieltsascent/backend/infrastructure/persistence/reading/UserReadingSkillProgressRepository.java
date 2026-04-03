package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingSkillStatus;
import com.ieltsascent.backend.domain.readingtest.ReadingSkillType;
import com.ieltsascent.backend.domain.readingtest.UserReadingSkillProgress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingSkillProgressRepository extends JpaRepository<UserReadingSkillProgress, UUID> {
    Optional<UserReadingSkillProgress> findByUserIdAndSkillTypeAndSkillKey(UUID userId, ReadingSkillType skillType, String skillKey);
    List<UserReadingSkillProgress> findByUserIdOrderByWeakScoreDesc(UUID userId);
    List<UserReadingSkillProgress> findByUserIdAndStatusIn(UUID userId, List<ReadingSkillStatus> statuses);
}
