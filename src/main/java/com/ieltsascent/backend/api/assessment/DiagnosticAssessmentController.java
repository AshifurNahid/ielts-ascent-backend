package com.ieltsascent.backend.api.assessment;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.application.assessment.AssessmentService;
import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessment/diagnostic")
@RequiredArgsConstructor
public class DiagnosticAssessmentController {
    private final AssessmentService assessmentService;

    @PostMapping("/start")
    public ApiResponse<DiagnosticSessionResponse> start() {
        var session = assessmentService.startDiagnostic();
        return ApiResponse.success(new DiagnosticSessionResponse(session.getId()));
    }

    @PostMapping("/submit-listening")
    public ApiResponse<SubmitResponse> submitListening(@Valid @RequestBody SubmitRequest request) {
        return ApiResponse.success(new SubmitResponse(request.sessionId(), "listening"));
    }

    @PostMapping("/submit-reading")
    public ApiResponse<SubmitResponse> submitReading(@Valid @RequestBody SubmitRequest request) {
        return ApiResponse.success(new SubmitResponse(request.sessionId(), "reading"));
    }

    @PostMapping("/submit-writing")
    public ApiResponse<SubmitResponse> submitWriting(@Valid @RequestBody SubmitRequest request) {
        return ApiResponse.success(new SubmitResponse(request.sessionId(), "writing"));
    }

    @PostMapping("/submit-speaking")
    public ApiResponse<SubmitResponse> submitSpeaking(@Valid @RequestBody SubmitRequest request) {
        DiagnosticResult result = assessmentService.completeDiagnostic(request.sessionId());
        return ApiResponse.success(new SubmitResponse(result.getSession().getId(), "speaking"));
    }

    @GetMapping("/{sessionId}/result")
    public ApiResponse<DiagnosticResultResponse> result(@PathVariable Long sessionId) {
        DiagnosticResult result = assessmentService.getResult(sessionId);
        List<MicroSkillScore> scores = assessmentService.listScores(sessionId);
        return ApiResponse.success(DiagnosticResultResponse.from(result, scores));
    }

    public record SubmitRequest(@NotNull Long sessionId) {
    }

    public record SubmitResponse(Long sessionId, String submittedSection) {
    }

    public record DiagnosticSessionResponse(Long sessionId) {
    }

    public record MicroSkillScoreDto(String skillName, Integer score, Integer confidence) {
        public static MicroSkillScoreDto from(MicroSkillScore score) {
            return new MicroSkillScoreDto(score.getSkillName(), score.getScore(), score.getConfidence());
        }
    }

    public record DiagnosticResultResponse(Long sessionId, Double overallBand, List<MicroSkillScoreDto> microSkills) {
        public static DiagnosticResultResponse from(DiagnosticResult result, List<MicroSkillScore> scores) {
            return new DiagnosticResultResponse(
                result.getSession().getId(),
                result.getOverallBand(),
                scores.stream().map(MicroSkillScoreDto::from).toList()
            );
        }
    }
}
