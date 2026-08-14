package com.ieltsascent.backend.api.mocktest;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.application.mocktest.MockTestService;
import com.ieltsascent.backend.domain.mocktest.MockTestOverallResult;
import com.ieltsascent.backend.domain.mocktest.MockTestSectionResult;
import com.ieltsascent.backend.domain.mocktest.MockTestSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock-tests")
@RequiredArgsConstructor
public class MockTestController {
    private final MockTestService mockTestService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<MockTestSessionResponse>> start(@Valid @RequestBody StartRequest request) {
        MockTestSession session = mockTestService.startSession(request.mode());
        return ApiResponseUtil.success(new MockTestSessionResponse(session.getId(), session.getMode()), ApiResponseConstant.CREATED);
    }

    @PostMapping("/{sessionId}/submit-section")
    public ResponseEntity<ApiResponse<MockTestSectionResponse>> submitSection(
        @PathVariable Long sessionId,
        @Valid @RequestBody SubmitSectionRequest request
    ) {
        MockTestSectionResult result = mockTestService.recordSection(
            sessionId,
            request.section(),
            request.bandScore(),
            request.feedback()
        );
        return ApiResponseUtil.success(new MockTestSectionResponse(result.getId(), result.getSection(), result.getBandScore()), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/{sessionId}/result")
    public ResponseEntity<ApiResponse<MockTestResultResponse>> result(@PathVariable Long sessionId) {
        MockTestOverallResult result = mockTestService.finalizeSession(sessionId);
        return ApiResponseUtil.success(new MockTestResultResponse(result.getSession().getId(), result.getOverallBand(), result.getSummary()), ApiResponseConstant.SUCCESS);
    }

    public record StartRequest(@NotBlank String mode) {
    }

    public record SubmitSectionRequest(
        @NotBlank String section,
        @NotNull Double bandScore,
        String feedback
    ) {
    }

    public record MockTestSessionResponse(Long sessionId, String mode) {
    }

    public record MockTestSectionResponse(Long sectionResultId, String section, Double bandScore) {
    }

    public record MockTestResultResponse(Long sessionId, Double overallBand, String summary) {
    }
}
