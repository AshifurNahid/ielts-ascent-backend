package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.UserWritingProgressService;
import com.ieltsascent.backend.application.writing.core.WritingWeakPointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingProgressController {
    private final WritingWeakPointService writingWeakPointService;
    private final UserWritingProgressService userWritingProgressService;

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<WritingDtos.ProgressResponse>> progress() {
        return ApiResponseUtil.success(WritingDtos.ProgressResponse.from(userWritingProgressService.getCurrent()), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/weak-points")
    public ResponseEntity<ApiResponse<List<WritingWeakPointService.RecurringWeakPointSummary>>> weakPoints() {
        return ApiResponseUtil.success(writingWeakPointService.recurringWeakPoints(), ApiResponseConstant.SUCCESS);
    }
}

