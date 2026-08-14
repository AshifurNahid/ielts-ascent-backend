package com.ieltsascent.backend.api.analytics;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.application.analytics.AnalyticsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> overview() {
        return ApiResponseUtil.success(analyticsService.overview(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/trends")
    public ResponseEntity<ApiResponse<Map<String, Object>>> trends() {
        return ApiResponseUtil.success(analyticsService.trends(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/weaknesses")
    public ResponseEntity<ApiResponse<Map<String, Object>>> weaknesses() {
        return ApiResponseUtil.success(analyticsService.weaknesses(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/exam-prediction")
    public ResponseEntity<ApiResponse<ExamPrediction>> prediction() {
        return ApiResponseUtil.success(analyticsService.examPrediction(), ApiResponseConstant.SUCCESS);
    }
}
