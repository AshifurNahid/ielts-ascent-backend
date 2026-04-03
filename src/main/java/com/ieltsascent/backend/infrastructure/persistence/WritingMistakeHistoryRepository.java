package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.WritingMistakeHistory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingMistakeHistoryRepository extends JpaRepository<WritingMistakeHistory, UUID> {
}
