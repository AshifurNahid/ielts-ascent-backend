package com.ieltsascent.backend.api.readingtest;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.readingtest.ReadingAiService;
import com.ieltsascent.backend.application.readingtest.ReadingUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reading/v2")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class ReadingUserController {
    private final ReadingUserService readingUserService;
    private final ReadingAiService readingAiService;

    @PutMapping("/profile")
    public ApiResponse<Void> upsertProfile(@Valid @RequestBody ReadingDtos.ReadingProfileUpsertRequest request) {
        readingUserService.upsertProfile(request);
        return ApiResponse.success(null);
    }

    @GetMapping("/profile")
    public ApiResponse<ReadingDtos.ReadingProfileResponse> profile() {
        return ApiResponse.success(readingUserService.profile());
    }

    @GetMapping("/tests")
    public ApiResponse<List<ReadingDtos.ReadingTestResponse>> availableTests() {
        return ApiResponse.success(readingUserService.availableTests());
    }

    @GetMapping("/tests/{id}")
    public ApiResponse<ReadingDtos.ReadingTestDetailResponse> testDetail(@PathVariable Long id) {
        return ApiResponse.success(readingUserService.testDetail(id));
    }

    @GetMapping("/practice/passages")
    public ApiResponse<List<ReadingDtos.ReadingPassageResponse>> passages() {
        return ApiResponse.success(readingUserService.passagesForPractice());
    }

    @GetMapping("/practice/questions")
    public ApiResponse<List<ReadingDtos.ReadingQuestionResponse>> questionsByType(@Valid @ModelAttribute ReadingDtos.QuestionsByTypeRequest request) {
        return ApiResponse.success(readingUserService.practiceByType(request.type()));
    }

    @GetMapping("/practice/passages/{passageId}/questions")
    public ApiResponse<List<ReadingDtos.ReadingQuestionResponse>> questionsByPassage(@PathVariable Long passageId) {
        return ApiResponse.success(readingUserService.practiceByPassage(passageId));
    }

    @PostMapping("/attempts")
    public ApiResponse<ReadingDtos.AttemptSummaryResponse> submitAttempt(@Valid @RequestBody ReadingDtos.AttemptSubmitRequest request) {
        return ApiResponse.success(readingUserService.submitAttempt(request));
    }

    @GetMapping("/attempts/{attemptId}")
    public ApiResponse<ReadingDtos.AttemptResultResponse> attemptResult(@PathVariable Long attemptId) {
        return ApiResponse.success(readingUserService.result(attemptId));
    }

    @GetMapping("/progress")
    public ApiResponse<List<ReadingDtos.SkillProgressResponse>> progress() {
        return ApiResponse.success(readingUserService.progress());
    }

    @GetMapping("/weak-areas")
    public ApiResponse<List<ReadingDtos.SkillProgressResponse>> weakAreas() {
        return ApiResponse.success(readingUserService.weakAreas());
    }

    @GetMapping("/recommendations")
    public ApiResponse<ReadingDtos.RecommendationResponse> recommendation() {
        return ApiResponse.success(readingUserService.recommendation());
    }

    @PostMapping("/ai/explain")
    public ApiResponse<ReadingDtos.ReadingAiExplainResponse> explain(@Valid @RequestBody ReadingDtos.ReadingAiExplainRequest request) {
        return ApiResponse.success(readingAiService.explain(request));
    }
}
