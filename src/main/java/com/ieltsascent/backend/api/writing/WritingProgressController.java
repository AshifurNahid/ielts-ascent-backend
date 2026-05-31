package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.UserWritingProgressService;
import com.ieltsascent.backend.application.writing.core.WritingWeakPointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<WritingDtos.ProgressResponse> progress() {
        return ApiResponse.success(WritingDtos.ProgressResponse.from(userWritingProgressService.getCurrent()));
    }

    @GetMapping("/weak-points")
    public ApiResponse<List<WritingWeakPointService.RecurringWeakPointSummary>> weakPoints() {
        return ApiResponse.success(writingWeakPointService.recurringWeakPoints());
    }
}

