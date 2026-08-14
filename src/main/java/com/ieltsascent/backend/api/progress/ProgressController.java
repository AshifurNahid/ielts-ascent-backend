package com.ieltsascent.backend.api.progress;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.DashboardResponse;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Prediction;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.SkillProgress;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeakArea;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeeklyActivity;
import com.ieltsascent.backend.application.progress.ProgressDashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressDashboardService progressDashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> dashboard() {
        return ApiResponseUtil.success(progressDashboardService.getDashboard(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/prediction")
    public ResponseEntity<ApiResponse<Prediction>> prediction() {
        return ApiResponseUtil.success(progressDashboardService.getPrediction(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<SkillProgress>>> skills() {
        return ApiResponseUtil.success(progressDashboardService.getSkills(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/weekly-activity")
    public ResponseEntity<ApiResponse<WeeklyActivity>> weeklyActivity() {
        return ApiResponseUtil.success(progressDashboardService.getWeeklyActivity(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/weak-areas")
    public ResponseEntity<ApiResponse<List<WeakArea>>> weakAreas() {
        return ApiResponseUtil.success(progressDashboardService.getWeakAreas(), ApiResponseConstant.SUCCESS);
    }
}
