package com.ieltsascent.backend.api.grammar;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.grammar.GrammarApiService;
import com.ieltsascent.backend.application.grammar.GrammarUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<List<GrammarDtos.TopicResponse>> topics() {
        return ApiResponse.success(grammarApiService.topics());
    }

    @GetMapping("/topics/{topicId}")
    public ApiResponse<GrammarDtos.TopicResponse> topic(@PathVariable Long topicId) {
        return ApiResponse.success(grammarApiService.topic(topicId));
    }

    @GetMapping("/topics/{topicId}/lessons")
    public ApiResponse<List<GrammarDtos.LessonResponse>> lessons(@PathVariable Long topicId) {
        return ApiResponse.success(grammarUserService.lessonsByTopic(topicId));
    }

    @GetMapping("/topics/{topicId}/questions")
    public ApiResponse<List<GrammarDtos.QuestionResponse>> topicQuestions(@PathVariable Long topicId) {
        return ApiResponse.success(grammarApiService.topicQuestions(topicId));
    }

    @GetMapping("/drills/templates")
    public ApiResponse<List<GrammarDtos.DrillTemplateResponse>> drills() {
        return ApiResponse.success(grammarApiService.drills());
    }

    @GetMapping("/drills/questions")
    public ApiResponse<List<GrammarDtos.QuestionResponse>> drillQuestions(
        @Valid @ModelAttribute GrammarDtos.DrillQuestionsRequest request
    ) {
        int limit = request.limit() == null ? 10 : request.limit();
        return ApiResponse.success(grammarApiService.drillQuestions(request.type(), limit));
    }

    @GetMapping("/progress")
    public ApiResponse<GrammarDtos.ProgressSummaryResponse> progress() {
        return ApiResponse.success(grammarUserService.progressSummary());
    }

    @PostMapping("/lessons/complete")
    public ApiResponse<Void> completeLesson(@Valid @RequestBody GrammarDtos.CompleteLessonRequest request) {
        grammarUserService.completeLesson(request.lessonId());
        return ApiResponse.success(null);
    }

    @PostMapping("/practice/submit")
    public ApiResponse<GrammarDtos.SubmitPracticeResponse> submitPractice(
        @Valid @RequestBody GrammarDtos.SubmitPracticeRequest request
    ) {
        return ApiResponse.success(grammarUserService.submitPractice(request));
    }

    @GetMapping("/recommendations")
    public ApiResponse<GrammarDtos.RecommendationResponse> recommendations(
        @RequestParam(defaultValue = "8") int limit
    ) {
        return ApiResponse.success(grammarApiService.recommendations(limit));
    }

    @GetMapping("/weak-areas")
    public ApiResponse<GrammarDtos.UserGrammarProfileResponse> weakAreas() {
        return ApiResponse.success(grammarApiService.profile());
    }

    @GetMapping("/profile")
    public ApiResponse<GrammarDtos.UserGrammarProfileResponse> getProfile() {
        return ApiResponse.success(grammarApiService.profile());
    }

    @PutMapping("/profile")
    public ApiResponse<GrammarDtos.UserGrammarProfileResponse> upsertProfile(
        @Valid @RequestBody GrammarDtos.UserGrammarProfileUpsertRequest request
    ) {
        return ApiResponse.success(grammarApiService.upsertProfile(request));
    }
}
