package com.ieltsascent.backend.api.analytics;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.application.analytics.AnalyticsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview(Authentication authentication) {
        return ApiResponse.success(analyticsService.overview(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/trends")
    public ApiResponse<Map<String, Object>> trends(Authentication authentication) {
        return ApiResponse.success(analyticsService.trends(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/weaknesses")
    public ApiResponse<Map<String, Object>> weaknesses(Authentication authentication) {
        return ApiResponse.success(analyticsService.weaknesses(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/exam-prediction")
    public ApiResponse<ExamPrediction> prediction(Authentication authentication) {
        return ApiResponse.success(analyticsService.examPrediction(SecurityUtils.currentUserId(authentication)));
    }
}
