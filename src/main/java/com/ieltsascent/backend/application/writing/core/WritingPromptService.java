package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingPromptStatus;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingPromptRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WritingPromptService {
    private final WritingPromptRepository writingPromptRepository;

    public Page<WritingPrompt> listPublished(WritingTaskType taskType, WritingDifficulty difficulty, Pageable pageable) {
        if (Objects.nonNull(taskType) && Objects.nonNull(difficulty)) {
            return writingPromptRepository.findByStatusAndTaskTypeAndDifficulty(WritingPromptStatus.PUBLISHED, taskType, difficulty, pageable);
        }
        if (Objects.nonNull(taskType)) {
            return writingPromptRepository.findByStatusAndTaskType(WritingPromptStatus.PUBLISHED, taskType, pageable);
        }
        if (Objects.nonNull(difficulty)) {
            return writingPromptRepository.findByStatusAndDifficulty(WritingPromptStatus.PUBLISHED, difficulty, pageable);
        }
        return writingPromptRepository.findByStatus(WritingPromptStatus.PUBLISHED, pageable);
    }

    public Page<WritingPrompt> adminList(Pageable pageable) {
        return writingPromptRepository.findAll(pageable);
    }

    public WritingPrompt get(Long id) {
        return writingPromptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Writing prompt not found"));
    }

    @Transactional
    public WritingPrompt create(WritingPrompt prompt) {
        validateBandRange(prompt.getIeltsBandMin(), prompt.getIeltsBandMax());
        return writingPromptRepository.save(prompt);
    }

    @Transactional
    public WritingPrompt update(Long id, WritingPrompt input) {
        validateBandRange(input.getIeltsBandMin(), input.getIeltsBandMax());
        WritingPrompt existing = get(id);
        existing.setTitle(input.getTitle());
        existing.setTaskType(input.getTaskType());
        existing.setPromptText(input.getPromptText());
        existing.setInstructions(input.getInstructions());
        existing.setTags(input.getTags());
        existing.setDifficulty(input.getDifficulty());
        existing.setIeltsBandMin(input.getIeltsBandMin());
        existing.setIeltsBandMax(input.getIeltsBandMax());
        existing.setPremium(input.isPremium());
        existing.setTemplateEnabled(input.isTemplateEnabled());
        if (Objects.nonNull(input.getStatus())) {
            existing.setStatus(input.getStatus());
        }
        return writingPromptRepository.save(existing);
    }

    private void validateBandRange(Double min, Double max) {
        if (Objects.isNull(min) || Objects.isNull(max)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IELTS band min and max are required");
        }
        if (min < 0.0 || max > 9.0 || min > max) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IELTS band range");
        }
    }

    @Transactional
    public WritingPrompt setStatus(Long id, WritingPromptStatus status) {
        WritingPrompt existing = get(id);
        existing.setStatus(status);
        return writingPromptRepository.save(existing);
    }
}
