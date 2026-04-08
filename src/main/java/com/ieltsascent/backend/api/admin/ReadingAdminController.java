package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.readingtest.ReadingAdminService;
import com.ieltsascent.backend.application.readingtest.ReadingAiService;
import com.ieltsascent.backend.application.readingtest.ReadingMapper;
import com.ieltsascent.backend.domain.readingtest.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reading")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReadingAdminController {
    private final ReadingAdminService adminService;
    private final ReadingAiService readingAiService;

    @PostMapping("/passages")
    public ApiResponse<ReadingDtos.ReadingPassageResponse> createPassage(@Valid @RequestBody ReadingDtos.PassageUpsertRequest request) {
        return ApiResponse.success(ReadingMapper.toPassageResponse(adminService.createPassage(request)));
    }

    @PutMapping("/passages/{id}")
    public ApiResponse<ReadingDtos.ReadingPassageResponse> updatePassage(@PathVariable Long id, @Valid @RequestBody ReadingDtos.PassageUpsertRequest request) {
        return ApiResponse.success(ReadingMapper.toPassageResponse(adminService.updatePassage(id, request)));
    }

    @PatchMapping("/passages/{id}/status")
    public ApiResponse<ReadingDtos.ReadingPassageResponse> updatePassageStatus(@PathVariable Long id, @Valid @RequestBody ReadingDtos.StatusUpdateRequest request) {
        return ApiResponse.success(ReadingMapper.toPassageResponse(adminService.updatePassageStatus(id, request.status())));
    }

    @GetMapping("/passages")
    public ApiResponse<PageResponse<ReadingDtos.ReadingPassageResponse>> listPassages(@RequestParam(required = false) ReadingContentStatus status,
                                                                                       @RequestParam(required = false) ReadingDifficulty difficulty,
                                                                                       @RequestParam(required = false) Boolean premium,
                                                                                       @RequestParam(required = false) String topicTag,
                                                                                       @RequestParam(required = false) String query,
                                                                                       @ParameterObject Pageable pageable) {
        return ApiResponse.success(PageResponse.from(adminService.listPassages(status, difficulty, premium, topicTag, query, pageable).map(ReadingMapper::toPassageResponse)));
    }

    @PostMapping("/questions")
    public ApiResponse<ReadingDtos.ReadingQuestionResponse> createQuestion(@Valid @RequestBody ReadingDtos.QuestionUpsertRequest request) {
        return ApiResponse.success(ReadingMapper.toQuestionResponse(adminService.createQuestion(request)));
    }

    @PutMapping("/questions/{id}")
    public ApiResponse<ReadingDtos.ReadingQuestionResponse> updateQuestion(@PathVariable Long id, @Valid @RequestBody ReadingDtos.QuestionUpsertRequest request) {
        return ApiResponse.success(ReadingMapper.toQuestionResponse(adminService.updateQuestion(id, request)));
    }

    @PatchMapping("/questions/{id}/status")
    public ApiResponse<ReadingDtos.ReadingQuestionResponse> updateQuestionStatus(@PathVariable Long id, @Valid @RequestBody ReadingDtos.StatusUpdateRequest request) {
        return ApiResponse.success(ReadingMapper.toQuestionResponse(adminService.updateQuestionStatus(id, request.status())));
    }

    @GetMapping("/questions")
    public ApiResponse<PageResponse<ReadingDtos.ReadingQuestionResponse>> listQuestions(@RequestParam(required = false) ReadingContentStatus status,
                                                                                         @RequestParam(required = false) ReadingQuestionKind type,
                                                                                          @RequestParam(required = false) Long passageId,
                                                                                         @RequestParam(required = false) Boolean premium,
                                                                                         @RequestParam(required = false) String query,
                                                                                         @ParameterObject Pageable pageable) {
        return ApiResponse.success(PageResponse.from(adminService.listQuestions(status, type, passageId, premium, query, pageable).map(ReadingMapper::toQuestionResponse)));
    }

    @PostMapping("/tests")
    public ApiResponse<ReadingDtos.ReadingTestResponse> createTest(@Valid @RequestBody ReadingDtos.TestUpsertRequest request) {
        return ApiResponse.success(ReadingMapper.toTestResponse(adminService.createTest(request)));
    }

    @PutMapping("/tests/{id}")
    public ApiResponse<ReadingDtos.ReadingTestResponse> updateTest(@PathVariable Long id, @Valid @RequestBody ReadingDtos.TestUpsertRequest request) {
        return ApiResponse.success(ReadingMapper.toTestResponse(adminService.updateTest(id, request)));
    }

    @PatchMapping("/tests/{id}/status")
    public ApiResponse<ReadingDtos.ReadingTestResponse> updateTestStatus(@PathVariable Long id, @Valid @RequestBody ReadingDtos.StatusUpdateRequest request) {
        return ApiResponse.success(ReadingMapper.toTestResponse(adminService.updateTestStatus(id, request.status())));
    }

    @GetMapping("/tests")
    public ApiResponse<PageResponse<ReadingDtos.ReadingTestResponse>> listTests(@RequestParam(required = false) ReadingContentStatus status,
                                                                                 @RequestParam(required = false) ReadingDifficulty difficulty,
                                                                                 @RequestParam(required = false) Boolean premium,
                                                                                 @RequestParam(required = false) String query,
                                                                                 @ParameterObject Pageable pageable) {
        return ApiResponse.success(PageResponse.from(adminService.listTests(status, difficulty, premium, query, pageable).map(ReadingMapper::toTestResponse)));
    }

    @PutMapping("/tests/{testId}/passages")
    public ApiResponse<Void> assignPassages(@PathVariable Long testId, @Valid @RequestBody ReadingDtos.AssignPassagesRequest request) {
        adminService.assignPassages(testId, request);
        return ApiResponse.success(null);
    }

    @PostMapping("/ai/enrich")
    public ApiResponse<ReadingDtos.AdminAiEnrichResponse> enrichDraft(@Valid @RequestBody ReadingDtos.AdminAiEnrichRequest request) {
        return ApiResponse.success(readingAiService.enrich(request));
    }
}
