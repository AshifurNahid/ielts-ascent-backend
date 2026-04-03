package com.ieltsascent.backend.api.story.dto;

import java.util.List;

public record StoryEvaluationResponse(
    double fluency,
    double coherence,
    double vocabulary,
    double grammar,
    List<String> strengths,
    List<String> improvements
) {
}
