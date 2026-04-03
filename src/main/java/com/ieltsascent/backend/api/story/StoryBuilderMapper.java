package com.ieltsascent.backend.api.story;

import com.ieltsascent.backend.api.story.dto.StoryEvaluationResponse;
import com.ieltsascent.backend.api.story.dto.StoryHistoryResponse;
import com.ieltsascent.backend.application.story.StoryBuilderService;
import com.ieltsascent.backend.application.story.StoryEvaluationEngine;

public interface StoryBuilderMapper {
    StoryEvaluationResponse toEvaluationResponse(StoryEvaluationEngine.StoryEvaluationResult result);

    StoryHistoryResponse toHistoryResponse(StoryBuilderService.StoryHistoryItem item);
}
