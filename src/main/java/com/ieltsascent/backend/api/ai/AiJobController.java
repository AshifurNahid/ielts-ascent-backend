package com.ieltsascent.backend.api.ai;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.application.ai.AiJobService;
import com.ieltsascent.backend.domain.ai.AiJob;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiJobController {
    private final AiJobService aiJobService;

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<AiJobResponse> getJob(Authentication authentication, @PathVariable UUID jobId) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        AiJob job = aiJobService.getJob(userId, jobId);
        return ApiResponse.success(new AiJobResponse(job.getId(), job.getStatus().name(), job.getOutputRef()));
    }

    public record AiJobResponse(UUID id, String status, UUID outputRef) {
    }
}
