package com.ieltsascent.backend.api.progress;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.DashboardResponse;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Prediction;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.SkillProgress;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeakArea;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeeklyActivity;
import com.ieltsascent.backend.application.progress.ProgressDashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressDashboardService progressDashboardService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard() {
        return ApiResponse.success(progressDashboardService.getDashboard());
    }

    @GetMapping("/prediction")
    public ApiResponse<Prediction> prediction() {
        return ApiResponse.success(progressDashboardService.getPrediction());
    }

    @GetMapping("/skills")
    public ApiResponse<List<SkillProgress>> skills() {
        return ApiResponse.success(progressDashboardService.getSkills());
    }

    @GetMapping("/weekly-activity")
    public ApiResponse<WeeklyActivity> weeklyActivity() {
        return ApiResponse.success(progressDashboardService.getWeeklyActivity());
    }

    @GetMapping("/weak-areas")
    public ApiResponse<List<WeakArea>> weakAreas() {
        return ApiResponse.success(progressDashboardService.getWeakAreas());
    }
}
