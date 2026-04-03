package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingPromptStatus;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingPromptRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WritingPromptService {
    private final WritingPromptRepository writingPromptRepository;

    public Page<WritingPrompt> listPublished(WritingTaskType taskType, WritingDifficulty difficulty, Pageable pageable) {
        if (taskType != null && difficulty != null) {
            return writingPromptRepository.findByStatusAndTaskTypeAndDifficulty(WritingPromptStatus.PUBLISHED, taskType, difficulty, pageable);
        }
        return writingPromptRepository.findByStatus(WritingPromptStatus.PUBLISHED, pageable);
    }

    public Page<WritingPrompt> adminList(Pageable pageable) {
        return writingPromptRepository.findAll(pageable);
    }

    public WritingPrompt get(UUID id) {
        return writingPromptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Writing prompt not found"));
    }

    @Transactional
    public WritingPrompt create(WritingPrompt prompt) {
        return writingPromptRepository.save(prompt);
    }

    @Transactional
    public WritingPrompt update(UUID id, WritingPrompt input) {
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
        if (input.getStatus() != null) {
            existing.setStatus(input.getStatus());
        }
        return writingPromptRepository.save(existing);
    }

    @Transactional
    public WritingPrompt setStatus(UUID id, WritingPromptStatus status) {
        WritingPrompt existing = get(id);
        existing.setStatus(status);
        return writingPromptRepository.save(existing);
    }
}
