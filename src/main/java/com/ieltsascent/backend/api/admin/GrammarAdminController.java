package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.grammar.GrammarAdminService;
import com.ieltsascent.backend.application.grammar.GrammarAiService;
import com.ieltsascent.backend.application.grammar.GrammarMapper;
import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarLevel;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/grammar")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class GrammarAdminController {
    private final GrammarAdminService adminService;
    private final GrammarAiService grammarAiService;

    @PostMapping("/topics")
    public ApiResponse<GrammarDtos.TopicResponse> createTopic(@Valid @RequestBody GrammarDtos.TopicUpsertRequest request) {
        return ApiResponse.success(GrammarMapper.toTopicResponse(adminService.createTopic(request)));
    }

    @PutMapping("/topics/{topicId}")
    public ApiResponse<GrammarDtos.TopicResponse> updateTopic(@PathVariable Long topicId, @Valid @RequestBody GrammarDtos.TopicUpsertRequest request) {
        return ApiResponse.success(GrammarMapper.toTopicResponse(adminService.updateTopic(topicId, request)));
    }

    @GetMapping("/topics")
    public ApiResponse<PageResponse<GrammarDtos.TopicResponse>> listTopics(
        @RequestParam(required = false) GrammarContentStatus status,
        @RequestParam(required = false) GrammarLevel level,
        @RequestParam(required = false) Boolean premium,
        @RequestParam(required = false) String query,
        @ParameterObject Pageable pageable
    ) {
        Page<GrammarDtos.TopicResponse> page = adminService.listTopics(status, level, premium, query, pageable).map(GrammarMapper::toTopicResponse);
        return ApiResponse.success(PageResponse.from(page));
    }

    @PostMapping("/lessons")
    public ApiResponse<GrammarDtos.LessonResponse> createLesson(@Valid @RequestBody GrammarDtos.LessonUpsertRequest request) {
        var lesson = adminService.createLesson(request);
        return ApiResponse.success(GrammarMapper.toLessonResponse(lesson, java.util.List.of()));
    }

    @PutMapping("/lessons/{lessonId}")
    public ApiResponse<GrammarDtos.LessonResponse> updateLesson(@PathVariable Long lessonId, @Valid @RequestBody GrammarDtos.LessonUpsertRequest request) {
        var lesson = adminService.updateLesson(lessonId, request);
        return ApiResponse.success(GrammarMapper.toLessonResponse(lesson, java.util.List.of()));
    }

    @PostMapping("/examples")
    public ApiResponse<GrammarDtos.LessonExampleResponse> createExample(@Valid @RequestBody GrammarDtos.LessonExampleUpsertRequest request) {
        return ApiResponse.success(GrammarMapper.toLessonExampleResponse(adminService.createLessonExample(request)));
    }

    @PutMapping("/examples/{exampleId}")
    public ApiResponse<GrammarDtos.LessonExampleResponse> updateExample(@PathVariable Long exampleId, @Valid @RequestBody GrammarDtos.LessonExampleUpsertRequest request) {
        return ApiResponse.success(GrammarMapper.toLessonExampleResponse(adminService.updateLessonExample(exampleId, request)));
    }

    @DeleteMapping("/examples/{exampleId}")
    public ApiResponse<Void> deleteExample(@PathVariable Long exampleId) {
        adminService.deleteLessonExample(exampleId);
        return ApiResponse.success(null);
    }

    @PostMapping("/questions")
    public ApiResponse<GrammarDtos.QuestionResponse> createQuestion(@Valid @RequestBody GrammarDtos.QuestionUpsertRequest request) {
        return ApiResponse.success(GrammarMapper.toQuestionResponse(adminService.createQuestion(request)));
    }

    @PutMapping("/questions/{questionId}")
    public ApiResponse<GrammarDtos.QuestionResponse> updateQuestion(@PathVariable Long questionId, @Valid @RequestBody GrammarDtos.QuestionUpsertRequest request) {
        return ApiResponse.success(GrammarMapper.toQuestionResponse(adminService.updateQuestion(questionId, request)));
    }

    @GetMapping("/questions")
    public ApiResponse<PageResponse<GrammarDtos.QuestionResponse>> listQuestions(
        @RequestParam(required = false) GrammarContentStatus status,
        @RequestParam(required = false) GrammarQuestionType type,
        @RequestParam(required = false) Long topicId,
        @RequestParam(required = false) Boolean premium,
        @RequestParam(required = false) String query,
        @ParameterObject Pageable pageable
    ) {
        Page<GrammarDtos.QuestionResponse> page = adminService.listQuestions(status, type, topicId, premium, query, pageable)
            .map(GrammarMapper::toQuestionResponse);
        return ApiResponse.success(PageResponse.from(page));
    }

    @PostMapping("/ai/lesson-draft")
    public ApiResponse<GrammarDtos.AiLessonEnrichmentResponse> lessonDraft(@Valid @RequestBody GrammarDtos.AiLessonEnrichmentRequest request) {
        return ApiResponse.success(grammarAiService.generateLessonDraft(request));
    }
}
