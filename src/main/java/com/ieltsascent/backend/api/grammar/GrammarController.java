package com.ieltsascent.backend.api.grammar;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.grammar.GrammarApiService;
import com.ieltsascent.backend.application.grammar.GrammarUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grammar")
@RequiredArgsConstructor
public class GrammarController {
    private final GrammarApiService grammarApiService;
    private final GrammarUserService grammarUserService;

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<GrammarDtos.TopicResponse>>> topics() {
        return ApiResponseUtil.success(grammarApiService.topics(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/topics/{topicId}")
    public ResponseEntity<ApiResponse<GrammarDtos.TopicResponse>> topic(@PathVariable Long topicId) {
        return ApiResponseUtil.success(grammarApiService.topic(topicId), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/topics/{topicId}/lessons")
    public ResponseEntity<ApiResponse<List<GrammarDtos.LessonResponse>>> lessons(@PathVariable Long topicId) {
        return ApiResponseUtil.success(grammarUserService.lessonsByTopic(topicId), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/topics/{topicId}/questions")
    public ResponseEntity<ApiResponse<List<GrammarDtos.QuestionResponse>>> topicQuestions(@PathVariable Long topicId) {
        return ApiResponseUtil.success(grammarApiService.topicQuestions(topicId), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/drills/templates")
    public ResponseEntity<ApiResponse<List<GrammarDtos.DrillTemplateResponse>>> drills() {
        return ApiResponseUtil.success(grammarApiService.drills(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/drills/questions")
    public ResponseEntity<ApiResponse<List<GrammarDtos.QuestionResponse>>> drillQuestions(
        @Valid @ModelAttribute GrammarDtos.DrillQuestionsRequest request
    ) {
        int limit = request.limit() == null ? 10 : request.limit();
        return ApiResponseUtil.success(grammarApiService.drillQuestions(request.type(), limit), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<GrammarDtos.ProgressSummaryResponse>> progress() {
        return ApiResponseUtil.success(grammarUserService.progressSummary(), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/lessons/complete")
    public ResponseEntity<ApiResponse<Void>> completeLesson(@Valid @RequestBody GrammarDtos.CompleteLessonRequest request) {
        grammarUserService.completeLesson(request.lessonId());
        return ApiResponseUtil.success(null, ApiResponseConstant.UPDATED);
    }

    @PostMapping("/practice/submit")
    public ResponseEntity<ApiResponse<GrammarDtos.SubmitPracticeResponse>> submitPractice(
        @Valid @RequestBody GrammarDtos.SubmitPracticeRequest request
    ) {
        return ApiResponseUtil.success(grammarUserService.submitPractice(request), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<GrammarDtos.RecommendationResponse>> recommendations(
        @RequestParam(defaultValue = "8") int limit
    ) {
        return ApiResponseUtil.success(grammarApiService.recommendations(limit), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/weak-areas")
    public ResponseEntity<ApiResponse<GrammarDtos.UserGrammarProfileResponse>> weakAreas() {
        return ApiResponseUtil.success(grammarApiService.profile(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<GrammarDtos.UserGrammarProfileResponse>> getProfile() {
        return ApiResponseUtil.success(grammarApiService.profile(), ApiResponseConstant.SUCCESS);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<GrammarDtos.UserGrammarProfileResponse>> upsertProfile(
        @Valid @RequestBody GrammarDtos.UserGrammarProfileUpsertRequest request
    ) {
        return ApiResponseUtil.success(grammarApiService.upsertProfile(request), ApiResponseConstant.UPDATED);
    }
}
