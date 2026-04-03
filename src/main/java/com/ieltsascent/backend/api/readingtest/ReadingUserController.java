package com.ieltsascent.backend.api.readingtest;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.readingtest.ReadingAiService;
import com.ieltsascent.backend.application.readingtest.ReadingUserService;
import com.ieltsascent.backend.domain.readingtest.ReadingQuestionKind;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reading/v2")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class ReadingUserController {
    private final ReadingUserService readingUserService;
    private final ReadingAiService readingAiService;

    @PutMapping("/profile")
    public ApiResponse<Void> upsertProfile(@Valid @RequestBody ReadingDtos.ReadingProfileUpsertRequest request, Authentication authentication) {
        readingUserService.upsertProfile(SecurityUtils.currentUserId(authentication), request);
        return ApiResponse.success(null);
    }

    @GetMapping("/profile")
    public ApiResponse<ReadingDtos.ReadingProfileResponse> profile(Authentication authentication) {
        return ApiResponse.success(readingUserService.profile(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/tests")
    public ApiResponse<List<ReadingDtos.ReadingTestResponse>> availableTests(Authentication authentication) {
        return ApiResponse.success(readingUserService.availableTests(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/tests/{id}")
    public ApiResponse<ReadingDtos.ReadingTestDetailResponse> testDetail(@PathVariable java.util.UUID id, Authentication authentication) {
        return ApiResponse.success(readingUserService.testDetail(SecurityUtils.currentUserId(authentication), id));
    }

    @GetMapping("/practice/passages")
    public ApiResponse<List<ReadingDtos.ReadingPassageResponse>> passages(Authentication authentication) {
        return ApiResponse.success(readingUserService.passagesForPractice(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/practice/questions")
    public ApiResponse<List<ReadingDtos.ReadingQuestionResponse>> questionsByType(@RequestParam ReadingQuestionKind type, Authentication authentication) {
        return ApiResponse.success(readingUserService.practiceByType(SecurityUtils.currentUserId(authentication), type));
    }

    @GetMapping("/practice/passages/{passageId}/questions")
    public ApiResponse<List<ReadingDtos.ReadingQuestionResponse>> questionsByPassage(@PathVariable UUID passageId,
                                                                                      Authentication authentication) {
        return ApiResponse.success(readingUserService.practiceByPassage(SecurityUtils.currentUserId(authentication), passageId));
    }

    @PostMapping("/attempts")
    public ApiResponse<ReadingDtos.AttemptSummaryResponse> submitAttempt(@Valid @RequestBody ReadingDtos.AttemptSubmitRequest request,
                                                                         Authentication authentication) {
        return ApiResponse.success(readingUserService.submitAttempt(SecurityUtils.currentUserId(authentication), request));
    }

    @GetMapping("/attempts/{attemptId}")
    public ApiResponse<ReadingDtos.AttemptResultResponse> attemptResult(@PathVariable java.util.UUID attemptId, Authentication authentication) {
        return ApiResponse.success(readingUserService.result(SecurityUtils.currentUserId(authentication), attemptId));
    }

    @GetMapping("/progress")
    public ApiResponse<List<ReadingDtos.SkillProgressResponse>> progress(Authentication authentication) {
        return ApiResponse.success(readingUserService.progress(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/weak-areas")
    public ApiResponse<List<ReadingDtos.SkillProgressResponse>> weakAreas(Authentication authentication) {
        return ApiResponse.success(readingUserService.weakAreas(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/recommendations")
    public ApiResponse<ReadingDtos.RecommendationResponse> recommendation(Authentication authentication) {
        return ApiResponse.success(readingUserService.recommendation(SecurityUtils.currentUserId(authentication)));
    }

    @PostMapping("/ai/explain")
    public ApiResponse<ReadingDtos.ReadingAiExplainResponse> explain(@Valid @RequestBody ReadingDtos.ReadingAiExplainRequest request) {
        return ApiResponse.success(readingAiService.explain(request));
    }
}
