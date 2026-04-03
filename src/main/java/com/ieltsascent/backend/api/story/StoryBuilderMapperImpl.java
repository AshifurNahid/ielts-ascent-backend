package com.ieltsascent.backend.api.story;

import com.ieltsascent.backend.api.story.dto.StoryEvaluationResponse;
import com.ieltsascent.backend.api.story.dto.StoryHistoryResponse;
import com.ieltsascent.backend.application.story.StoryBuilderService;
import com.ieltsascent.backend.application.story.StoryEvaluationEngine;
import org.springframework.stereotype.Component;

@Component
public class StoryBuilderMapperImpl implements StoryBuilderMapper {
    @Override
    public StoryEvaluationResponse toEvaluationResponse(StoryEvaluationEngine.StoryEvaluationResult result) {
        return new StoryEvaluationResponse(
            result.fluency(),
            result.coherence(),
            result.vocabulary(),
            result.grammar(),
            result.strengths(),
            result.improvements()
        );
    }

    @Override
    public StoryHistoryResponse toHistoryResponse(StoryBuilderService.StoryHistoryItem item) {
        return new StoryHistoryResponse(
            item.submissionId(),
            item.topic(),
            item.wordCount(),
            item.submittedAt(),
            item.overallBand()
        );
    }
}
