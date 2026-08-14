package com.ieltsascent.backend.api.story;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.story.dto.EvaluateStoryRequest;
import com.ieltsascent.backend.api.story.dto.StoryEvaluationResponse;
import com.ieltsascent.backend.api.story.dto.StoryHistoryResponse;
import com.ieltsascent.backend.api.story.dto.SubmitStoryRequest;
import com.ieltsascent.backend.api.story.dto.SubmitStoryResponse;
import com.ieltsascent.backend.application.story.StoryBuilderApiService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/practice/speaking/story-builder")
@RequiredArgsConstructor
public class StoryBuilderController {
    private final StoryBuilderApiService storyBuilderApiService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitStoryResponse>> submit(
        @Valid @RequestBody SubmitStoryRequest request
    ) {
        return ApiResponseUtil.success(storyBuilderApiService.submit(request.topic(), request.parts()), ApiResponseConstant.CREATED);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<StoryEvaluationResponse>> evaluate(
        @Valid @RequestBody EvaluateStoryRequest request
    ) {
        return ApiResponseUtil.success(storyBuilderApiService.evaluate(request.submissionId()), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<StoryHistoryResponse>>> history() {
        return ApiResponseUtil.success(storyBuilderApiService.history(), ApiResponseConstant.SUCCESS);
    }
}
