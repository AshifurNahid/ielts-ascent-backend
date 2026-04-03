package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingTemplateRepository extends JpaRepository<WritingTemplate, UUID> {
    List<WritingTemplate> findByActiveTrueAndTaskType(WritingTaskType taskType);

    List<WritingTemplate> findByActiveTrueAndPromptId(UUID promptId);
}
