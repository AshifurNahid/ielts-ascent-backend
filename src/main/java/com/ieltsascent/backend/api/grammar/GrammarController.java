package com.ieltsascent.backend.api.grammar;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.grammar.GrammarMapper;
import com.ieltsascent.backend.application.grammar.GrammarUserService;
import com.ieltsascent.backend.application.grammar.UserGrammarProfileService;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final GrammarUserService grammarUserService;
    private final UserGrammarProfileService userGrammarProfileService;

    @GetMapping("/topics")
    public ApiResponse<List<GrammarDtos.TopicResponse>> topics(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(grammarUserService.getTopics(userId).stream().map(GrammarMapper::toTopicResponse).toList());
    }

    @GetMapping("/topics/{topicId}")
    public ApiResponse<GrammarDtos.TopicResponse> topic(Authentication authentication, @PathVariable UUID topicId) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(GrammarMapper.toTopicResponse(grammarUserService.getTopic(userId, topicId)));
    }

    @GetMapping("/topics/{topicId}/lessons")
    public ApiResponse<List<GrammarDtos.LessonResponse>> lessons(Authentication authentication, @PathVariable UUID topicId) {
        return ApiResponse.success(grammarUserService.lessonsByTopic(SecurityUtils.currentUserId(authentication), topicId));
    }

    @GetMapping("/topics/{topicId}/questions")
    public ApiResponse<List<GrammarDtos.QuestionResponse>> topicQuestions(Authentication authentication, @PathVariable UUID topicId) {
        return ApiResponse.success(grammarUserService.getTopicQuestions(SecurityUtils.currentUserId(authentication), topicId)
            .stream().map(GrammarMapper::toQuestionResponse).toList());
    }

    @GetMapping("/drills/templates")
    public ApiResponse<List<GrammarDtos.DrillTemplateResponse>> drills() {
        return ApiResponse.success(grammarUserService.activeDrills().stream().map(GrammarMapper::toDrillTemplateResponse).toList());
    }

    @GetMapping("/drills/questions")
    public ApiResponse<List<GrammarDtos.QuestionResponse>> drillQuestions(
        Authentication authentication,
        @RequestParam GrammarQuestionType type,
        @RequestParam(defaultValue = "10") int limit
    ) {
        return ApiResponse.success(grammarUserService.getDrillQuestions(SecurityUtils.currentUserId(authentication), type, Math.min(limit, 50))
            .stream().map(GrammarMapper::toQuestionResponse).toList());
    }

    @GetMapping("/progress")
    public ApiResponse<GrammarDtos.ProgressSummaryResponse> progress(Authentication authentication) {
        return ApiResponse.success(grammarUserService.progressSummary(SecurityUtils.currentUserId(authentication)));
    }

    @PostMapping("/lessons/complete")
    public ApiResponse<Void> completeLesson(Authentication authentication, @Valid @RequestBody GrammarDtos.CompleteLessonRequest request) {
        grammarUserService.completeLesson(SecurityUtils.currentUserId(authentication), request.lessonId());
        return ApiResponse.success(null);
    }

    @PostMapping("/practice/submit")
    public ApiResponse<GrammarDtos.SubmitPracticeResponse> submitPractice(
        Authentication authentication,
        @Valid @RequestBody GrammarDtos.SubmitPracticeRequest request
    ) {
        return ApiResponse.success(grammarUserService.submitPractice(SecurityUtils.currentUserId(authentication), request));
    }

    @GetMapping("/recommendations")
    public ApiResponse<GrammarDtos.RecommendationResponse> recommendations(
        Authentication authentication,
        @RequestParam(defaultValue = "8") int limit
    ) {
        return ApiResponse.success(grammarUserService.recommendations(SecurityUtils.currentUserId(authentication), Math.min(limit, 20)));
    }

    @GetMapping("/weak-areas")
    public ApiResponse<GrammarDtos.UserGrammarProfileResponse> weakAreas(Authentication authentication) {
        return ApiResponse.success(GrammarMapper.toProfileResponse(userGrammarProfileService.getOrCreate(SecurityUtils.currentUserId(authentication))));
    }

    @GetMapping("/profile")
    public ApiResponse<GrammarDtos.UserGrammarProfileResponse> getProfile(Authentication authentication) {
        return ApiResponse.success(GrammarMapper.toProfileResponse(userGrammarProfileService.getOrCreate(SecurityUtils.currentUserId(authentication))));
    }

    @PutMapping("/profile")
    public ApiResponse<GrammarDtos.UserGrammarProfileResponse> upsertProfile(
        Authentication authentication,
        @Valid @RequestBody GrammarDtos.UserGrammarProfileUpsertRequest request
    ) {
        return ApiResponse.success(GrammarMapper.toProfileResponse(userGrammarProfileService.upsert(SecurityUtils.currentUserId(authentication), request)));
    }
}
