package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.writing.UserWritingProgress;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import com.ieltsascent.backend.infrastructure.persistence.writing.UserWritingProgressRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWritingProgressServiceTest {
    @Mock
    private UserWritingProgressRepository progressRepository;
    @Mock
    private WritingSubmissionRepository submissionRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private UserWritingProgressService service;

    @Test
    void recalculatesProgressFromEvaluatedSubmissions() {
        User user = new User();

        List<WritingSubmission> recentByNewestFirst = List.of(
            submission(7.0, 1),
            submission(6.5, 3),
            submission(6.0, 5),
            submission(5.5, 8),
            submission(5.0, 12)
        );
        List<WritingSubmission> last30DaysByOldestFirst = List.of(
            submission(5.0, 12),
            submission(5.5, 8),
            submission(6.0, 5),
            submission(6.5, 3),
            submission(7.0, 1)
        );

        when(submissionRepository.findTop20ByUserIdAndStatusOrderBySubmittedAtDesc(any(), eq(WritingSubmissionStatus.EVALUATED)))
            .thenReturn(recentByNewestFirst);
        when(submissionRepository.countByUserIdAndStatus(any(), eq(WritingSubmissionStatus.EVALUATED)))
            .thenReturn(5L);
        when(progressRepository.findByUserId(any())).thenReturn(Optional.empty());
        when(submissionRepository.findByUserIdAndStatusAndSubmittedAtAfterOrderBySubmittedAtAsc(
            any(), eq(WritingSubmissionStatus.EVALUATED), any(Instant.class)))
            .thenReturn(last30DaysByOldestFirst);
        when(progressRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.recalculate(user);

        ArgumentCaptor<UserWritingProgress> captor = ArgumentCaptor.forClass(UserWritingProgress.class);
        verify(progressRepository).save(captor.capture());
        UserWritingProgress progress = captor.getValue();
        assertThat(progress.getTotalSubmissions()).isEqualTo(5L);
        assertThat(progress.getAverageBand()).isEqualTo(6.0);
        assertThat(progress.getBestBand()).isEqualTo(7.0);
        assertThat(progress.getLatestBand()).isEqualTo(7.0);
        assertThat(progress.getTrendValue()).isEqualTo(-0.5);
        assertThat(progress.getLast30DayImprovement()).isEqualTo(1.0);
        assertThat(progress.getLast30DayNetImprovement()).isEqualTo(2.0);
        assertThat(progress.getLast30DayVolatility()).isEqualTo(0.71);
    }

    @Test
    void keepsNeutralStatsForSingleSubmission() {
        User user = new User();

        when(submissionRepository.findTop20ByUserIdAndStatusOrderBySubmittedAtDesc(any(), eq(WritingSubmissionStatus.EVALUATED)))
            .thenReturn(List.of(submission(6.0, 1)));
        when(submissionRepository.countByUserIdAndStatus(any(), eq(WritingSubmissionStatus.EVALUATED)))
            .thenReturn(1L);
        when(progressRepository.findByUserId(any())).thenReturn(Optional.empty());
        when(submissionRepository.findByUserIdAndStatusAndSubmittedAtAfterOrderBySubmittedAtAsc(
            any(), eq(WritingSubmissionStatus.EVALUATED), any(Instant.class)))
            .thenReturn(List.of(submission(6.0, 1)));
        when(progressRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service.recalculate(user);

        ArgumentCaptor<UserWritingProgress> captor = ArgumentCaptor.forClass(UserWritingProgress.class);
        verify(progressRepository).save(captor.capture());
        UserWritingProgress progress = captor.getValue();
        assertThat(progress.getAverageBand()).isEqualTo(6.0);
        assertThat(progress.getBestBand()).isEqualTo(6.0);
        assertThat(progress.getLatestBand()).isEqualTo(6.0);
        assertThat(progress.getTrendValue()).isEqualTo(0.0);
        assertThat(progress.getLast30DayImprovement()).isEqualTo(0.0);
        assertThat(progress.getLast30DayNetImprovement()).isEqualTo(0.0);
        assertThat(progress.getLast30DayVolatility()).isEqualTo(0.0);
    }

    private WritingSubmission submission(double overallBand, long daysAgo) {
        WritingSubmission submission = new WritingSubmission();
        submission.setStatus(WritingSubmissionStatus.EVALUATED);
        submission.setOverallBand(overallBand);
        submission.setSubmittedAt(Instant.now().minus(daysAgo, ChronoUnit.DAYS));
        return submission;
    }
}
