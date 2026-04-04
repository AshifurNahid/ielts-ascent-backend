package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import java.util.List;

public interface AiTextAnalysisClient {

    String generateTemplateForUserStyle(User user, List<WritingSubmission> history);
}
