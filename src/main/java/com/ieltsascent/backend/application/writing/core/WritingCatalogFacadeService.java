package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WritingCatalogFacadeService {
    private final WritingPromptService writingPromptService;
    private final WritingTemplateService writingTemplateService;

    @Transactional(readOnly = true)
    public PageResponse<WritingDtos.PromptResponse> listPrompts(WritingTaskType taskType, WritingDifficulty difficulty, Pageable pageable) {
        Page<WritingDtos.PromptResponse> page = writingPromptService
            .listPublished(taskType, difficulty, pageable)
            .map(WritingDtos.PromptResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public WritingDtos.PromptResponse getPrompt(Long id) {
        return WritingDtos.PromptResponse.from(writingPromptService.get(id));
    }

    @Transactional(readOnly = true)
    public List<WritingDtos.TemplateResponse> listTemplates(WritingTaskType taskType, Long promptId) {
        return writingTemplateService.listActive(taskType, promptId).stream().map(WritingDtos.TemplateResponse::from).toList();
    }
}
