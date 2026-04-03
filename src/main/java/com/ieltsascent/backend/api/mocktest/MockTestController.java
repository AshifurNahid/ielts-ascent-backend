package com.ieltsascent.backend.api.mocktest;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.application.mocktest.MockTestService;
import com.ieltsascent.backend.domain.mocktest.MockTestOverallResult;
import com.ieltsascent.backend.domain.mocktest.MockTestSectionResult;
import com.ieltsascent.backend.domain.mocktest.MockTestSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public ApiResponse<MockTestSessionResponse> start(Authentication authentication, @Valid @RequestBody StartRequest request) {
        MockTestSession session = mockTestService.startSession(SecurityUtils.currentUserId(authentication), request.mode());
        return ApiResponse.success(new MockTestSessionResponse(session.getId(), session.getMode()));
    }

    @PostMapping("/{sessionId}/submit-section")
    public ApiResponse<MockTestSectionResponse> submitSection(
        @PathVariable UUID sessionId,
        @Valid @RequestBody SubmitSectionRequest request
    ) {
        MockTestSectionResult result = mockTestService.recordSection(
            sessionId,
            request.section(),
            request.bandScore(),
            request.feedback()
        );
        return ApiResponse.success(new MockTestSectionResponse(result.getId(), result.getSection(), result.getBandScore()));
    }

    @GetMapping("/{sessionId}/result")
    public ApiResponse<MockTestResultResponse> result(@PathVariable UUID sessionId) {
        MockTestOverallResult result = mockTestService.finalizeSession(sessionId);
        return ApiResponse.success(new MockTestResultResponse(result.getSession().getId(), result.getOverallBand(), result.getSummary()));
    }

    public record StartRequest(@NotBlank String mode) {
    }

    public record SubmitSectionRequest(
        @NotBlank String section,
        @NotNull Double bandScore,
        String feedback
    ) {
    }

    public record MockTestSessionResponse(UUID sessionId, String mode) {
    }

    public record MockTestSectionResponse(UUID sectionResultId, String section, Double bandScore) {
    }

    public record MockTestResultResponse(UUID sessionId, Double overallBand, String summary) {
    }
}
