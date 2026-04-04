package com.ieltsascent.backend.api.admin.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.WritingPromptService;
import com.ieltsascent.backend.application.writing.core.WritingTemplateService;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingPromptStatus;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/writing")
@RequiredArgsConstructor
public class AdminWritingController {
    private final WritingPromptService writingPromptService;
    private final WritingTemplateService writingTemplateService;

    @GetMapping("/prompts")
    public ApiResponse<PageResponse<WritingDtos.PromptResponse>> prompts(@ParameterObject Pageable pageable) {
        Page<WritingDtos.PromptResponse> page = writingPromptService.adminList(pageable).map(WritingDtos.PromptResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @PostMapping("/prompts")
    public ApiResponse<WritingDtos.PromptResponse> createPrompt(@Valid @RequestBody WritingDtos.AdminPromptRequest request) {
        return ApiResponse.success(WritingDtos.PromptResponse.from(writingPromptService.create(toPrompt(request))));
    }

    @PutMapping("/prompts/{id}")
    public ApiResponse<WritingDtos.PromptResponse> updatePrompt(@PathVariable UUID id, @Valid @RequestBody WritingDtos.AdminPromptRequest request) {
        return ApiResponse.success(WritingDtos.PromptResponse.from(writingPromptService.update(id, toPrompt(request))));
    }

    @PutMapping("/prompts/{id}/status")
    public ApiResponse<WritingDtos.PromptResponse> setPromptStatus(@PathVariable UUID id, @RequestParam WritingPromptStatus status) {
        return ApiResponse.success(WritingDtos.PromptResponse.from(writingPromptService.setStatus(id, status)));
    }

    @GetMapping("/templates")
    public ApiResponse<List<WritingDtos.TemplateResponse>> templates(
        @RequestParam(required = false) WritingTaskType taskType,
        @RequestParam(required = false) UUID promptId
    ) {
        return ApiResponse.success(writingTemplateService.listActive(taskType, promptId).stream().map(WritingDtos.TemplateResponse::from).toList());
    }

    @PostMapping("/templates")
    public ApiResponse<WritingDtos.TemplateResponse> createTemplate(@Valid @RequestBody WritingDtos.AdminTemplateRequest request) {
        return ApiResponse.success(WritingDtos.TemplateResponse.from(writingTemplateService.create(toTemplate(request))));
    }

    @PutMapping("/templates/{id}")
    public ApiResponse<WritingDtos.TemplateResponse> updateTemplate(@PathVariable UUID id, @Valid @RequestBody WritingDtos.AdminTemplateRequest request) {
        return ApiResponse.success(WritingDtos.TemplateResponse.from(writingTemplateService.update(id, toTemplate(request))));
    }

    @DeleteMapping("/templates/{id}")
    public ApiResponse<Void> archiveTemplate(@PathVariable UUID id) {
        writingTemplateService.archive(id);
        return ApiResponse.success(null);
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
        try {
            return Enum.valueOf(enumType, rawValue);
        } catch (IllegalArgumentException _) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + fieldName + ": " + rawValue);
        }
    }
}
