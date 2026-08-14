package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingPassageResponse>> createPassage(@Valid @RequestBody ReadingDtos.PassageUpsertRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toPassageResponse(adminService.createPassage(request)), ApiResponseConstant.CREATED, HttpStatus.CREATED);
    }

    @PutMapping("/passages/{id}")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingPassageResponse>> updatePassage(@PathVariable Long id, @Valid @RequestBody ReadingDtos.PassageUpsertRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toPassageResponse(adminService.updatePassage(id, request)), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/passages/{id}/status")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingPassageResponse>> updatePassageStatus(@PathVariable Long id, @Valid @RequestBody ReadingDtos.StatusUpdateRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toPassageResponse(adminService.updatePassageStatus(id, request.status())), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/passages")
    public ResponseEntity<ApiResponse<PageResponse<ReadingDtos.ReadingPassageResponse>>> listPassages(
        @Valid @ModelAttribute ReadingDtos.PassageFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        return ApiResponseUtil.success(
            PageResponse.from(
                adminService
                    .listPassages(filter.status(), filter.difficulty(), filter.premium(), filter.topicTag(), filter.query(), pageable)
                    .map(ReadingMapper::toPassageResponse)
            ), ApiResponseConstant.SUCCESS
        );
    }

    @PostMapping("/questions")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingQuestionResponse>> createQuestion(@Valid @RequestBody ReadingDtos.QuestionUpsertRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toQuestionResponse(adminService.createQuestion(request)), ApiResponseConstant.CREATED, HttpStatus.CREATED);
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingQuestionResponse>> updateQuestion(@PathVariable Long id, @Valid @RequestBody ReadingDtos.QuestionUpsertRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toQuestionResponse(adminService.updateQuestion(id, request)), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/questions/{id}/status")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingQuestionResponse>> updateQuestionStatus(@PathVariable Long id, @Valid @RequestBody ReadingDtos.StatusUpdateRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toQuestionResponse(adminService.updateQuestionStatus(id, request.status())), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<PageResponse<ReadingDtos.ReadingQuestionResponse>>> listQuestions(
        @Valid @ModelAttribute ReadingDtos.QuestionFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        return ApiResponseUtil.success(
            PageResponse.from(
                adminService
                    .listQuestions(filter.status(), filter.type(), filter.passageId(), filter.premium(), filter.query(), pageable)
                    .map(ReadingMapper::toQuestionResponse)
            ), ApiResponseConstant.SUCCESS
        );
    }

    @PostMapping("/tests")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingTestResponse>> createTest(@Valid @RequestBody ReadingDtos.TestUpsertRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toTestResponse(adminService.createTest(request)), ApiResponseConstant.CREATED, HttpStatus.CREATED);
    }

    @PutMapping("/tests/{id}")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingTestResponse>> updateTest(@PathVariable Long id, @Valid @RequestBody ReadingDtos.TestUpsertRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toTestResponse(adminService.updateTest(id, request)), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/tests/{id}/status")
    public ResponseEntity<ApiResponse<ReadingDtos.ReadingTestResponse>> updateTestStatus(@PathVariable Long id, @Valid @RequestBody ReadingDtos.StatusUpdateRequest request) {
        return ApiResponseUtil.success(ReadingMapper.toTestResponse(adminService.updateTestStatus(id, request.status())), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/tests")
    public ResponseEntity<ApiResponse<PageResponse<ReadingDtos.ReadingTestResponse>>> listTests(
        @Valid @ModelAttribute ReadingDtos.TestFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        return ApiResponseUtil.success(
            PageResponse.from(
                adminService
                    .listTests(filter.status(), filter.difficulty(), filter.premium(), filter.query(), pageable)
                    .map(ReadingMapper::toTestResponse)
            ), ApiResponseConstant.SUCCESS
        );
    }

    @PutMapping("/tests/{testId}/passages")
    public ResponseEntity<ApiResponse<Void>> assignPassages(@PathVariable Long testId, @Valid @RequestBody ReadingDtos.AssignPassagesRequest request) {
        adminService.assignPassages(testId, request);
        return ApiResponseUtil.success(null, ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/ai/enrich")
    public ResponseEntity<ApiResponse<ReadingDtos.AdminAiEnrichResponse>> enrichDraft(@Valid @RequestBody ReadingDtos.AdminAiEnrichRequest request) {
        return ApiResponseUtil.success(readingAiService.enrich(request), ApiResponseConstant.SUCCESS);
    }
}
