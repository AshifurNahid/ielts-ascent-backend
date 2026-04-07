package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingTemplateRepository extends JpaRepository<WritingTemplate, Long> {
    List<WritingTemplate> findByActiveTrue();

    List<WritingTemplate> findByActiveTrueAndTaskType(WritingTaskType taskType);

    List<WritingTemplate> findByActiveTrueAndPromptId(Long promptId);

    List<WritingTemplate> findByActiveTrueAndTaskTypeAndPromptId(WritingTaskType taskType, Long promptId);
}
