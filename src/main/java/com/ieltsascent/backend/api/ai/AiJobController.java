package com.ieltsascent.backend.api.ai;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.application.ai.AiJobService;
import com.ieltsascent.backend.domain.ai.AiJob;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<AiJobResponse>> getJob(@PathVariable Long jobId) {
        AiJob job = aiJobService.getJob(jobId);
        return ApiResponseUtil.success(new AiJobResponse(job.getId(), job.getStatus().name(), job.getOutputRef()), ApiResponseConstant.SUCCESS);
    }

    public record AiJobResponse(Long id, String status, UUID outputRef) {
    }
}
