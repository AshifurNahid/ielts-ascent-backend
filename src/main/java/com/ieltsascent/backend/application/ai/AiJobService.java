package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.domain.ai.AiJob;
import com.ieltsascent.backend.infrastructure.persistence.AiJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiJobService {
    private final AiJobRepository aiJobRepository;

    public AiJob getJob(Long userId, Long jobId) {
        return aiJobRepository.findByIdAndUserId(jobId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
    }
}
