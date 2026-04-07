package com.ieltsascent.backend.api.story;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.story.dto.EvaluateStoryRequest;
import com.ieltsascent.backend.api.story.dto.StoryEvaluationResponse;
import com.ieltsascent.backend.api.story.dto.StoryHistoryResponse;
import com.ieltsascent.backend.api.story.dto.SubmitStoryRequest;
import com.ieltsascent.backend.api.story.dto.SubmitStoryResponse;
import com.ieltsascent.backend.application.story.StoryBuilderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/practice/speaking/story-builder")
@RequiredArgsConstructor
public class StoryBuilderController {
    private final StoryBuilderService storyBuilderService;
    private final StoryBuilderMapper storyBuilderMapper;

    @PostMapping("/submit")
    public ApiResponse<SubmitStoryResponse> submit(
        Authentication authentication,
        @Valid @RequestBody SubmitStoryRequest request
    ) {
        Long userId = SecurityUtils.currentUserId(authentication);
        var submission = storyBuilderService.submit(userId, request.topic(), request.parts());
        return ApiResponse.success(new SubmitStoryResponse(submission.getId(), submission.getWordCount()));
    }

    @PostMapping("/evaluate")
    public ApiResponse<StoryEvaluationResponse> evaluate(
        Authentication authentication,
        @Valid @RequestBody EvaluateStoryRequest request
    ) {
        Long userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(
            storyBuilderMapper.toEvaluationResponse(storyBuilderService.evaluate(userId, request.submissionId()))
        );
    }

    @GetMapping("/history")
    public ApiResponse<List<StoryHistoryResponse>> history(Authentication authentication) {
        Long userId = SecurityUtils.currentUserId(authentication);
        List<StoryHistoryResponse> items = storyBuilderService.history(userId)
            .stream()
            .map(storyBuilderMapper::toHistoryResponse)
            .toList();
        return ApiResponse.success(items);
    }
}
