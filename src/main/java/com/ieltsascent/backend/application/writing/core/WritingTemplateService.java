package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingTemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WritingTemplateService {
    private final WritingTemplateRepository writingTemplateRepository;

    public List<WritingTemplate> listActive(WritingTaskType taskType, Long promptId) {
        if (taskType != null && promptId != null) {
            return writingTemplateRepository.findByActiveTrueAndTaskTypeAndPromptId(taskType, promptId);
        }
        if (promptId != null) {
            return writingTemplateRepository.findByActiveTrueAndPromptId(promptId);
        }
        if (taskType != null) {
            return writingTemplateRepository.findByActiveTrueAndTaskType(taskType);
        }
        return writingTemplateRepository.findByActiveTrue();
    }

    @Transactional
    public WritingTemplate create(WritingTemplate template) {
        return writingTemplateRepository.save(template);
    }

    @Transactional
    public WritingTemplate update(Long id, WritingTemplate input) {
        WritingTemplate existing = writingTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Writing template not found"));
        existing.setTitle(input.getTitle());
        existing.setDescription(input.getDescription());
        existing.setTaskType(input.getTaskType());
        existing.setTemplateContent(input.getTemplateContent());
        existing.setTargetBand(input.getTargetBand());
        existing.setActive(input.isActive());
        existing.setPrompt(input.getPrompt());
        return writingTemplateRepository.save(existing);
    }

    @Transactional
    public void archive(Long id) {
        WritingTemplate existing = writingTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Writing template not found"));
        existing.setActive(false);
        writingTemplateRepository.save(existing);
    }
}
