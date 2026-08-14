package com.ieltsascent.backend.application.story;

import com.ieltsascent.backend.api.story.StoryBuilderMapper;
import com.ieltsascent.backend.api.story.dto.StoryEvaluationResponse;
import com.ieltsascent.backend.api.story.dto.StoryHistoryResponse;
import com.ieltsascent.backend.api.story.dto.SubmitStoryResponse;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryBuilderApiService {
    private final StoryBuilderService storyBuilderService;
    private final StoryBuilderMapper storyBuilderMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public SubmitStoryResponse submit(Long userId, String topic, Map<String, String> parts) {
        var submission = storyBuilderService.submit(userId, topic, parts);
        return new SubmitStoryResponse(submission.getId(), submission.getWordCount());
    }

    @Transactional
    public SubmitStoryResponse submit(String topic, Map<String, String> parts) {
        return submit(currentUserProvider.userId(), topic, parts);
    }

    @Transactional
    public StoryEvaluationResponse evaluate(Long userId, Long submissionId) {
        return storyBuilderMapper.toEvaluationResponse(storyBuilderService.evaluate(userId, submissionId));
    }

    @Transactional
    public StoryEvaluationResponse evaluate(Long submissionId) {
        return evaluate(currentUserProvider.userId(), submissionId);
    }

    @Transactional(readOnly = true)
    public List<StoryHistoryResponse> history(Long userId) {
        return storyBuilderService.history(userId).stream().map(storyBuilderMapper::toHistoryResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<StoryHistoryResponse> history() {
        return history(currentUserProvider.userId());
    }
}

