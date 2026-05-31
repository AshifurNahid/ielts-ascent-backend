package com.ieltsascent.backend.api.analytics;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.application.analytics.AnalyticsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
        return ApiResponse.success(analyticsService.overview());
    }

    @GetMapping("/trends")
    public ApiResponse<Map<String, Object>> trends() {
        return ApiResponse.success(analyticsService.trends());
    }

    @GetMapping("/weaknesses")
    public ApiResponse<Map<String, Object>> weaknesses() {
        return ApiResponse.success(analyticsService.weaknesses());
    }

    @GetMapping("/exam-prediction")
    public ApiResponse<ExamPrediction> prediction() {
        return ApiResponse.success(analyticsService.examPrediction());
    }
}
