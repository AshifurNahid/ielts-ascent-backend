package com.ieltsascent.backend.api.studyplan;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.application.studyplan.StudyPlanService;
import com.ieltsascent.backend.application.studyplan.dto.StudyTaskItemDto;
import com.ieltsascent.backend.domain.studyplan.StudyPlan;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study-plan")
@RequiredArgsConstructor
public class StudyPlanController {
    private final StudyPlanService studyPlanService;

    @PostMapping("/generate")
    public ApiResponse<StudyPlanResponse> generate(@Valid @RequestBody GenerateRequest request) {
        StudyPlan plan = studyPlanService.generateFromDiagnostic(request.diagnosticSessionId());
        return ApiResponse.success(StudyPlanResponse.from(plan));
    }

    @GetMapping("/current")
    public ApiResponse<StudyPlanResponse> current() {
        StudyPlan plan = studyPlanService.getCurrent();
        return ApiResponse.success(StudyPlanResponse.from(plan));
    }

    @GetMapping("/today-task")
    public ApiResponse<PageResponse<StudyTaskItemDto>> today(@ParameterObject Pageable pageable) {
        return ApiResponse.success(studyPlanService.getTodayTaskPage(pageable));
    }

    @PostMapping("/tasks/{taskId}/complete")
    public ApiResponse<TaskCompletionResponse> complete(@PathVariable Long taskId) {
        var completion = studyPlanService.completeTask(taskId);
        return ApiResponse.success(new TaskCompletionResponse(taskId, completion.getCompletedAt().toString()));
    }

    @PostMapping("/generate-crash-plan")
    public ApiResponse<CrashPlanResponse> generateCrashPlan() {
        List<StudyTaskResponse> tasks = studyPlanService.generateCrashPlan()
            .stream()
            .map(StudyTaskResponse::from)
            .toList();
        return ApiResponse.success(new CrashPlanResponse(Instant.now(), tasks));
    }

    public record GenerateRequest(@NotNull Long diagnosticSessionId) {
    }

    public record StudyPlanResponse(Long planId, String status) {
        public static StudyPlanResponse from(StudyPlan plan) {
            return new StudyPlanResponse(plan.getId(), plan.getStatus());
        }
    }

    public record StudyTaskResponse(Long id, String title, String taskType, Integer estimatedMinutes) {
        public static StudyTaskResponse from(StudyTask task) {
            return new StudyTaskResponse(task.getId(), task.getTitle(), task.getTaskType(), task.getEstimatedMinutes());
        }
    }

    public record TaskCompletionResponse(Long taskId, String completedAt) {
    }

    public record CrashPlanResponse(Instant generatedAt, List<StudyTaskResponse> tasks) {
    }
}
