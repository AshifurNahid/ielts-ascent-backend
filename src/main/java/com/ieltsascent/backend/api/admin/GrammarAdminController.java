package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.grammar.GrammarAdminService;
import com.ieltsascent.backend.application.grammar.GrammarAiService;
import com.ieltsascent.backend.application.grammar.GrammarMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/grammar")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class GrammarAdminController {
    private final GrammarAdminService adminService;
    private final GrammarAiService grammarAiService;

    @PostMapping("/topics")
    public ResponseEntity<ApiResponse<GrammarDtos.TopicResponse>> createTopic(@Valid @RequestBody GrammarDtos.TopicUpsertRequest request) {
        return ApiResponseUtil.success(GrammarMapper.toTopicResponse(adminService.createTopic(request)), ApiResponseConstant.CREATED);
    }

    @PutMapping("/topics/{topicId}")
    public ResponseEntity<ApiResponse<GrammarDtos.TopicResponse>> updateTopic(@PathVariable Long topicId, @Valid @RequestBody GrammarDtos.TopicUpsertRequest request) {
        return ApiResponseUtil.success(GrammarMapper.toTopicResponse(adminService.updateTopic(topicId, request)), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<PageResponse<GrammarDtos.TopicResponse>>> listTopics(
        @Valid @ModelAttribute GrammarDtos.TopicFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        Page<GrammarDtos.TopicResponse> page = adminService
            .listTopics(filter.status(), filter.level(), filter.premium(), filter.query(), pageable)
            .map(GrammarMapper::toTopicResponse);
        return ApiResponseUtil.success(PageResponse.from(page), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/lessons")
    public ResponseEntity<ApiResponse<GrammarDtos.LessonResponse>> createLesson(@Valid @RequestBody GrammarDtos.LessonUpsertRequest request) {
        var lesson = adminService.createLesson(request);
        return ApiResponseUtil.success(GrammarMapper.toLessonResponse(lesson, java.util.List.of()), ApiResponseConstant.CREATED);
    }

    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<ApiResponse<GrammarDtos.LessonResponse>> updateLesson(@PathVariable Long lessonId, @Valid @RequestBody GrammarDtos.LessonUpsertRequest request) {
        var lesson = adminService.updateLesson(lessonId, request);
        return ApiResponseUtil.success(GrammarMapper.toLessonResponse(lesson, java.util.List.of()), ApiResponseConstant.UPDATED);
    }

    @PostMapping("/examples")
    public ResponseEntity<ApiResponse<GrammarDtos.LessonExampleResponse>> createExample(@Valid @RequestBody GrammarDtos.LessonExampleUpsertRequest request) {
        return ApiResponseUtil.success(GrammarMapper.toLessonExampleResponse(adminService.createLessonExample(request)), ApiResponseConstant.CREATED);
    }

    @PutMapping("/examples/{exampleId}")
    public ResponseEntity<ApiResponse<GrammarDtos.LessonExampleResponse>> updateExample(@PathVariable Long exampleId, @Valid @RequestBody GrammarDtos.LessonExampleUpsertRequest request) {
        return ApiResponseUtil.success(GrammarMapper.toLessonExampleResponse(adminService.updateLessonExample(exampleId, request)), ApiResponseConstant.UPDATED);
    }

    @DeleteMapping("/examples/{exampleId}")
    public ResponseEntity<ApiResponse<Void>> deleteExample(@PathVariable Long exampleId) {
        adminService.deleteLessonExample(exampleId);
        return ApiResponseUtil.success(null, ApiResponseConstant.DELETED);
    }

    @PostMapping("/questions")
    public ResponseEntity<ApiResponse<GrammarDtos.QuestionResponse>> createQuestion(@Valid @RequestBody GrammarDtos.QuestionUpsertRequest request) {
        return ApiResponseUtil.success(GrammarMapper.toQuestionResponse(adminService.createQuestion(request)), ApiResponseConstant.CREATED);
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<GrammarDtos.QuestionResponse>> updateQuestion(@PathVariable Long questionId, @Valid @RequestBody GrammarDtos.QuestionUpsertRequest request) {
        return ApiResponseUtil.success(GrammarMapper.toQuestionResponse(adminService.updateQuestion(questionId, request)), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<PageResponse<GrammarDtos.QuestionResponse>>> listQuestions(
        @Valid @ModelAttribute GrammarDtos.QuestionFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        Page<GrammarDtos.QuestionResponse> page = adminService
            .listQuestions(filter.status(), filter.type(), filter.topicId(), filter.premium(), filter.query(), pageable)
            .map(GrammarMapper::toQuestionResponse);
        return ApiResponseUtil.success(PageResponse.from(page), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/ai/lesson-draft")
    public ResponseEntity<ApiResponse<GrammarDtos.AiLessonEnrichmentResponse>> lessonDraft(@Valid @RequestBody GrammarDtos.AiLessonEnrichmentRequest request) {
        return ApiResponseUtil.success(grammarAiService.generateLessonDraft(request), ApiResponseConstant.SUCCESS);
    }
}
