package com.ieltsascent.backend.application.writing;

import com.ieltsascent.backend.application.ai.WritingAiPort;
import com.ieltsascent.backend.domain.ai.AiJob;
import com.ieltsascent.backend.domain.ai.AiJobStatus;
import com.ieltsascent.backend.domain.ai.AiJobType;
import com.ieltsascent.backend.domain.content.WritingPrompt;
import com.ieltsascent.backend.domain.practice.WritingEvaluationResult;
import com.ieltsascent.backend.domain.practice.WritingSubmission;
import com.ieltsascent.backend.domain.practice.WritingSubmissionStatus;
import com.ieltsascent.backend.infrastructure.persistence.AiJobRepository;
import com.ieltsascent.backend.infrastructure.persistence.WritingEvaluationResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.WritingSubmissionRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WritingEvaluationJobServiceTest {
    @Mock
    private AiJobRepository aiJobRepository;

    @Mock
    private WritingSubmissionRepository submissionRepository;

    @Mock
    private WritingEvaluationResultRepository evaluationResultRepository;

    @Mock
    private WritingAiPort writingAiPort;

    @Captor
    private ArgumentCaptor<AiJob> jobCaptor;

    @Captor
    private ArgumentCaptor<WritingSubmission> submissionCaptor;

    @Captor
    private ArgumentCaptor<WritingEvaluationResult> evaluationCaptor;

    @InjectMocks
    private WritingEvaluationJobService jobService;

    @Test
    void marksJobSucceededOnSuccess() {
        UUID jobId = UUID.randomUUID();
        UUID submissionId = UUID.randomUUID();
        AiJob job = new AiJob();
        job.setUserId(UUID.randomUUID());
        job.setType(AiJobType.WRITING_EVAL);
        job.setStatus(AiJobStatus.PENDING);
        job.setInputRef(submissionId);
        job.setAttempts(0);

        WritingPrompt prompt = new WritingPrompt();
        prompt.setTaskType("TASK_2");
        WritingSubmission submission = new WritingSubmission();
        submission.setPrompt(prompt);
        submission.setEssayText("Essay");

        ObjectNode feedback = new ObjectMapper().createObjectNode();
        feedback.put("summary", "ok");
        feedback.putArray("mistakes");
        feedback.putArray("rewriteExercises");
        feedback.putArray("improvementTips");

        WritingAiResult aiResult = new WritingAiResult(6.5, 6.0, 6.5, 6.0, 6.5, feedback, "mock", "model", "v1");

        when(aiJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(writingAiPort.evaluateWriting(any(), any(), any(), anyDouble())).thenReturn(aiResult);
        when(evaluationResultRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(aiJobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(submissionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        jobService.processJobAsync(jobId, 7.0);

        verify(aiJobRepository, atLeastOnce()).save(jobCaptor.capture());
        verify(submissionRepository).save(submissionCaptor.capture());
        verify(evaluationResultRepository).save(evaluationCaptor.capture());

        AiJob finalJob = jobCaptor.getAllValues().get(jobCaptor.getAllValues().size() - 1);
        assertThat(finalJob.getStatus()).isEqualTo(AiJobStatus.SUCCEEDED);
        assertThat(submissionCaptor.getValue().getStatus()).isEqualTo(WritingSubmissionStatus.EVALUATED);
        assertThat(evaluationCaptor.getValue().getOverallBand()).isEqualTo(6.5);
    }
}
