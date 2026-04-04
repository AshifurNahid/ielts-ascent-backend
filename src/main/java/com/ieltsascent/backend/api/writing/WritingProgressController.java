package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.UserWritingProgressService;
import com.ieltsascent.backend.application.writing.core.WritingWeakPointService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingProgressController {
    private final WritingWeakPointService writingWeakPointService;
    private final UserWritingProgressService userWritingProgressService;

    @GetMapping("/progress")
    public ApiResponse<WritingDtos.ProgressResponse> progress(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(WritingDtos.ProgressResponse.from(userWritingProgressService.getByUserId(userId)));
    }

    @GetMapping("/weak-points")
    public ApiResponse<List<WritingWeakPointService.RecurringWeakPointSummary>> weakPoints(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(writingWeakPointService.recurringWeakPoints(userId));
    }
}

