package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingPromptStatus;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import java.util.List;
import java.util.Locale;
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
public class AdminWritingFacadeService {
    private final WritingPromptService writingPromptService;
    private final WritingTemplateService writingTemplateService;

    @Transactional(readOnly = true)
    public PageResponse<WritingDtos.PromptResponse> prompts(Pageable pageable) {
        Page<WritingDtos.PromptResponse> page = writingPromptService.adminList(pageable).map(WritingDtos.PromptResponse::from);
        return PageResponse.from(page);
    }

    @Transactional
    public WritingDtos.PromptResponse createPrompt(WritingDtos.AdminPromptRequest request) {
        return WritingDtos.PromptResponse.from(writingPromptService.create(toPrompt(request)));
    }

    @Transactional
    public WritingDtos.PromptResponse updatePrompt(Long id, WritingDtos.AdminPromptRequest request) {
        return WritingDtos.PromptResponse.from(writingPromptService.update(id, toPrompt(request)));
    }

    @Transactional
    public WritingDtos.PromptResponse setPromptStatus(Long id, WritingPromptStatus status) {
        return WritingDtos.PromptResponse.from(writingPromptService.setStatus(id, status));
    }

    @Transactional(readOnly = true)
    public List<WritingDtos.TemplateResponse> templates(WritingTaskType taskType, Long promptId) {
        return writingTemplateService.listActive(taskType, promptId).stream().map(WritingDtos.TemplateResponse::from).toList();
    }

    @Transactional
    public WritingDtos.TemplateResponse createTemplate(WritingDtos.AdminTemplateRequest request) {
        return WritingDtos.TemplateResponse.from(writingTemplateService.create(toTemplate(request)));
    }

    @Transactional
    public WritingDtos.TemplateResponse updateTemplate(Long id, WritingDtos.AdminTemplateRequest request) {
        return WritingDtos.TemplateResponse.from(writingTemplateService.update(id, toTemplate(request)));
    }

    @Transactional
    public void archiveTemplate(Long id) {
        writingTemplateService.archive(id);
    }

    private WritingPrompt toPrompt(WritingDtos.AdminPromptRequest request) {
        WritingPrompt prompt = new WritingPrompt();
        prompt.setTitle(request.title());
        prompt.setTaskType(parseEnum(WritingTaskType.class, request.taskType(), "taskType"));
        prompt.setPromptText(request.promptText());
        prompt.setInstructions(request.instructions());
        prompt.setTags(request.tags() == null ? List.of() : request.tags());
        prompt.setDifficulty(parseEnum(WritingDifficulty.class, request.difficulty(), "difficulty"));
        prompt.setIeltsBandMin(request.ieltsBandMin());
        prompt.setIeltsBandMax(request.ieltsBandMax());
        prompt.setPremium(request.premium());
        prompt.setStatus(parseEnum(WritingPromptStatus.class, request.status(), "status"));
        prompt.setTemplateEnabled(request.templateEnabled());
        return prompt;
    }

    private WritingTemplate toTemplate(WritingDtos.AdminTemplateRequest request) {
        WritingTemplate template = new WritingTemplate();
        template.setTaskType(parseEnum(WritingTaskType.class, request.taskType(), "taskType"));
        template.setTitle(request.title());
        template.setDescription(request.description());
        template.setTemplateContent(request.templateContent());
        template.setTargetBand(request.targetBand());
        template.setActive(request.active());
        if (request.promptId() != null) {
            template.setPrompt(writingPromptService.get(request.promptId()));
        }
        return template;
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumType, String rawValue, String fieldName) {
        if (Objects.isNull(rawValue) || rawValue.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }
        try {
            return Enum.valueOf(enumType, rawValue.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException _) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + fieldName + ": " + rawValue);
        }
    }
}
