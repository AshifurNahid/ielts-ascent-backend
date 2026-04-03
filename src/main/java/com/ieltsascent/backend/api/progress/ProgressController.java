package com.ieltsascent.backend.api.progress;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.DashboardResponse;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Prediction;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.SkillProgress;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeakArea;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeeklyActivity;
import com.ieltsascent.backend.application.progress.ProgressDashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressDashboardService progressDashboardService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard(Authentication authentication) {
        return ApiResponse.success(progressDashboardService.getDashboard(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/prediction")
    public ApiResponse<Prediction> prediction(Authentication authentication) {
        return ApiResponse.success(progressDashboardService.getPrediction(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/skills")
    public ApiResponse<List<SkillProgress>> skills(Authentication authentication) {
        return ApiResponse.success(progressDashboardService.getSkills(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/weekly-activity")
    public ApiResponse<WeeklyActivity> weeklyActivity(Authentication authentication) {
        return ApiResponse.success(progressDashboardService.getWeeklyActivity(SecurityUtils.currentUserId(authentication)));
    }

    @GetMapping("/weak-areas")
    public ApiResponse<List<WeakArea>> weakAreas(Authentication authentication) {
        return ApiResponse.success(progressDashboardService.getWeakAreas(SecurityUtils.currentUserId(authentication)));
    }
}
