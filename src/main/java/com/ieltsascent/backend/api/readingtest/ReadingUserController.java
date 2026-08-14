package com.ieltsascent.backend.api.readingtest;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Void>> upsertProfile(@Valid @RequestBody ReadingDtos.ReadingProfileUpsertRequest request) {
        readingUserService.upsertProfile(request);
        return ApiResponseUtil.success(null, ApiResponseConstant.PROFILE_UPSERTED);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingProfileResponse>> profile() {
        return ApiResponseUtil.success(readingUserService.profile(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/tests")
    public ResponseEntity<ApiResponse<List<ReadingDtos.ReadingTestResponse>>> availableTests() {
        return ApiResponseUtil.success(readingUserService.availableTests(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/tests/{id}")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingTestDetailResponse>> testDetail(@PathVariable Long id) {
        return ApiResponseUtil.success(readingUserService.testDetail(id), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/practice/passages")
    public ResponseEntity<ApiResponse<List<ReadingDtos.ReadingPassageResponse>>> passages() {
        return ApiResponseUtil.success(readingUserService.passagesForPractice(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/practice/questions")
    public ResponseEntity<ApiResponse<List<ReadingDtos.ReadingQuestionResponse>>> questionsByType(@Valid @ModelAttribute ReadingDtos.QuestionsByTypeRequest request) {
        return ApiResponseUtil.success(readingUserService.practiceByType(request.type()), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/practice/passages/{passageId}/questions")
    public ResponseEntity<ApiResponse<List<ReadingDtos.ReadingQuestionResponse>>> questionsByPassage(@PathVariable Long passageId) {
        return ApiResponseUtil.success(readingUserService.practiceByPassage(passageId), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/attempts")
    public ResponseEntity<ApiResponse<ReadingDtos.AttemptSummaryResponse>> submitAttempt(@Valid @RequestBody ReadingDtos.AttemptSubmitRequest request) {
        return ApiResponseUtil.success(readingUserService.submitAttempt(request), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/attempts/{attemptId}")
    public ResponseEntity<ApiResponse<ReadingDtos.AttemptResultResponse>> attemptResult(@PathVariable Long attemptId) {
        return ApiResponseUtil.success(readingUserService.result(attemptId), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<List<ReadingDtos.SkillProgressResponse>>> progress() {
        return ApiResponseUtil.success(readingUserService.progress(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/weak-areas")
    public ResponseEntity<ApiResponse<List<ReadingDtos.SkillProgressResponse>>> weakAreas() {
        return ApiResponseUtil.success(readingUserService.weakAreas(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<ReadingDtos.RecommendationResponse>> recommendation() {
        return ApiResponseUtil.success(readingUserService.recommendation(), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/ai/explain")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingAiExplainResponse>> explain(@Valid @RequestBody ReadingDtos.ReadingAiExplainRequest request) {
        return ApiResponseUtil.success(readingAiService.explain(request), ApiResponseConstant.SUCCESS);
    }
}
