package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingSkillStatus;
import com.ieltsascent.backend.domain.readingtest.ReadingSkillType;
import com.ieltsascent.backend.domain.readingtest.UserReadingSkillProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingSkillProgressRepository extends JpaRepository<UserReadingSkillProgress, Long> {
    Optional<UserReadingSkillProgress> findByUserIdAndSkillTypeAndSkillKey(Long userId, ReadingSkillType skillType, String skillKey);
    List<UserReadingSkillProgress> findByUserIdOrderByWeakScoreDesc(Long userId);
    List<UserReadingSkillProgress> findByUserIdAndStatusIn(Long userId, List<ReadingSkillStatus> statuses);
}
