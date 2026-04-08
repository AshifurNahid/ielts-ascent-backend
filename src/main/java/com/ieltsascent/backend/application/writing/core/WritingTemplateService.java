package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingTemplateRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WritingTemplateService {
    private final WritingTemplateRepository writingTemplateRepository;

    @Transactional(readOnly = true)
    public List<WritingTemplate> listActive(WritingTaskType taskType, Long promptId) {
        if (Objects.nonNull(taskType) && Objects.nonNull(promptId)) {
            return writingTemplateRepository.findByActiveTrueAndTaskTypeAndPromptId(taskType, promptId);
        }
        if (Objects.nonNull(promptId)) {
            return writingTemplateRepository.findByActiveTrueAndPromptId(promptId);
        }
        if (Objects.nonNull(taskType)) {
            return writingTemplateRepository.findByActiveTrueAndTaskType(taskType);
        }
        return writingTemplateRepository.findByActiveTrue();
    }

    @Transactional
    public WritingTemplate create(WritingTemplate template) {
        validateTargetBand(template.getTargetBand());
        return writingTemplateRepository.save(template);
    }

    @Transactional
    public WritingTemplate update(Long id, WritingTemplate input) {
        validateTargetBand(input.getTargetBand());
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

    private void validateTargetBand(Double targetBand) {
        if (Objects.nonNull(targetBand) && (targetBand < 0.0 || targetBand > 9.0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "targetBand must be between 0 and 9");
        }
    }

    @Transactional
    public void archive(Long id) {
        WritingTemplate existing = writingTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Writing template not found"));
        existing.setActive(false);
        writingTemplateRepository.save(existing);
    }
}
