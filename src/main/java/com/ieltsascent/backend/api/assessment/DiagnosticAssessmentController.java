package com.ieltsascent.backend.api.assessment;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.application.assessment.AssessmentService;
import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<DiagnosticSessionResponse>> start() {
        var session = assessmentService.startDiagnostic();
        return ApiResponseUtil.success(new DiagnosticSessionResponse(session.getId()), ApiResponseConstant.CREATED);
    }

    @PostMapping("/submit-listening")
    public ResponseEntity<ApiResponse<SubmitResponse>> submitListening(@Valid @RequestBody SubmitRequest request) {
        return ApiResponseUtil.success(new SubmitResponse(request.sessionId(), "listening"), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/submit-reading")
    public ResponseEntity<ApiResponse<SubmitResponse>> submitReading(@Valid @RequestBody SubmitRequest request) {
        return ApiResponseUtil.success(new SubmitResponse(request.sessionId(), "reading"), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/submit-writing")
    public ResponseEntity<ApiResponse<SubmitResponse>> submitWriting(@Valid @RequestBody SubmitRequest request) {
        return ApiResponseUtil.success(new SubmitResponse(request.sessionId(), "writing"), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/submit-speaking")
    public ResponseEntity<ApiResponse<SubmitResponse>> submitSpeaking(@Valid @RequestBody SubmitRequest request) {
        DiagnosticResult result = assessmentService.completeDiagnostic(request.sessionId());
        return ApiResponseUtil.success(new SubmitResponse(result.getSession().getId(), "speaking"), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/{sessionId}/result")
    public ResponseEntity<ApiResponse<DiagnosticResultResponse>> result(@PathVariable Long sessionId) {
        DiagnosticResult result = assessmentService.getResult(sessionId);
        List<MicroSkillScore> scores = assessmentService.listScores(sessionId);
        return ApiResponseUtil.success(DiagnosticResultResponse.from(result, scores), ApiResponseConstant.SUCCESS);
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
