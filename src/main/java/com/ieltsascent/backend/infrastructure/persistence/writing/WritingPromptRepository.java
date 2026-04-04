package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingPromptStatus;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingPromptRepository extends JpaRepository<WritingPrompt, UUID> {
    Page<WritingPrompt> findByStatus(WritingPromptStatus status, Pageable pageable);

    Page<WritingPrompt> findByStatusAndTaskType(WritingPromptStatus status, WritingTaskType taskType, Pageable pageable);

    Page<WritingPrompt> findByStatusAndDifficulty(WritingPromptStatus status, WritingDifficulty difficulty, Pageable pageable);

    Page<WritingPrompt> findByStatusAndTaskTypeAndDifficulty(
        WritingPromptStatus status,
        WritingTaskType taskType,
        WritingDifficulty difficulty,
        Pageable pageable
    );
}
