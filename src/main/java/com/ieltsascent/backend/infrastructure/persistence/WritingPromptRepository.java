package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.content.WritingPrompt;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingPromptRepository extends JpaRepository<WritingPrompt, UUID> {
    Page<WritingPrompt> findByTaskTypeAndCategory(String taskType, String category, Pageable pageable);

    Page<WritingPrompt> findByTaskType(String taskType, Pageable pageable);
}
