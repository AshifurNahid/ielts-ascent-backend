package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.practice.WritingEvaluationResult;
import com.ieltsascent.backend.domain.practice.WritingSubmission;
import java.util.List;

public interface AiTextAnalysisClient {
    WritingEvaluationResult evaluateWriting(String prompt, String userEssay);

    String suggestImprovements(String userEssay, WritingEvaluationResult current);

    String generateTemplateForUserStyle(User user, List<WritingSubmission> history);
}
