package com.ieltsascent.backend.api.story;

import com.ieltsascent.backend.api.story.dto.EvaluateStoryRequest;
import com.ieltsascent.backend.api.story.dto.StoryEvaluationResponse;
import com.ieltsascent.backend.api.story.dto.StoryHistoryResponse;
import com.ieltsascent.backend.api.story.dto.SubmitStoryRequest;
import com.ieltsascent.backend.application.story.StoryBuilderService;
import com.ieltsascent.backend.application.story.StoryEvaluationEngine;
import com.ieltsascent.backend.domain.story.StorySubmission;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StoryBuilderControllerTest {

    private StoryBuilderService storyBuilderService;
    private StoryBuilderMapper storyBuilderMapper;
    private StoryBuilderController controller;

    @BeforeEach
    void setUp() {
        storyBuilderService = mock(StoryBuilderService.class);
        storyBuilderMapper = mock(StoryBuilderMapper.class);
        controller = new StoryBuilderController(storyBuilderService, storyBuilderMapper);
    }

    @Test
    void submitDelegatesToService() {
        UUID userId = UUID.randomUUID();
        Authentication authentication = auth(userId);
        SubmitStoryRequest request = new SubmitStoryRequest("Topic", Map.of("setup", "Hello world"));

        StorySubmission submission = new StorySubmission();
        submission.setWordCount(2);
        when(storyBuilderService.submit(eq(userId), eq("Topic"), eq(request.parts()))).thenReturn(submission);

        var response = controller.submit(authentication, request);

        assertThat(response.data().wordCount()).isEqualTo(2);
        verify(storyBuilderService).submit(eq(userId), eq("Topic"), eq(request.parts()));
    }

    @Test
    void evaluateUsesMapperOutput() {
        UUID userId = UUID.randomUUID();
        UUID submissionId = UUID.randomUUID();
        Authentication authentication = auth(userId);

        StoryEvaluationEngine.StoryEvaluationResult result = new StoryEvaluationEngine.StoryEvaluationResult(
            6.0,
            6.5,
            6.0,
            6.5,
            6.0,
            List.of("S1", "S2"),
            List.of("I1", "I2")
        );
        StoryEvaluationResponse mapped = new StoryEvaluationResponse(6.0, 6.5, 6.0, 6.5, List.of("S1", "S2"), List.of("I1", "I2"));

        when(storyBuilderService.evaluate(userId, submissionId)).thenReturn(result);
        when(storyBuilderMapper.toEvaluationResponse(result)).thenReturn(mapped);

        var response = controller.evaluate(authentication, new EvaluateStoryRequest(submissionId));

        assertThat(response.data()).isEqualTo(mapped);
        verify(storyBuilderService).evaluate(userId, submissionId);
        verify(storyBuilderMapper).toEvaluationResponse(result);
    }

    @Test
    void historyMapsEachItem() {
        UUID userId = UUID.randomUUID();
        Authentication authentication = auth(userId);

        StoryBuilderService.StoryHistoryItem item = new StoryBuilderService.StoryHistoryItem(
            UUID.randomUUID(),
            "Topic",
            120,
            Instant.now(),
            6.5
        );
        StoryHistoryResponse mapped = new StoryHistoryResponse(item.submissionId(), item.topic(), item.wordCount(), item.submittedAt(), item.overallBand());

        when(storyBuilderService.history(userId)).thenReturn(List.of(item));
        when(storyBuilderMapper.toHistoryResponse(any())).thenReturn(mapped);

        var response = controller.history(authentication);

        assertThat(response.data()).containsExactly(mapped);
        verify(storyBuilderService).history(userId);
        verify(storyBuilderMapper).toHistoryResponse(item);
    }

    private Authentication auth(UUID userId) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userId);
        return authentication;
    }
}
