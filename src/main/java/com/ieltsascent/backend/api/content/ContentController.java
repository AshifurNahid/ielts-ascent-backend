package com.ieltsascent.backend.api.content;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.application.content.ContentService;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/listening")
    public ResponseEntity<ApiResponse<PageResponse<ListeningAudioDto>>> listening(@ParameterObject Pageable pageable) {
        Page<ListeningAudioDto> page = contentService.listListeningAudios(pageable)
            .map(ListeningAudioDto::from);
        return ApiResponseUtil.success(PageResponse.from(page), ApiResponseConstant.SUCCESS);
    }


    public record ListeningAudioDto(Long id, String title, String description, Integer durationSeconds, String audioUrl) {
        public static ListeningAudioDto from(ListeningAudio audio) {
            return new ListeningAudioDto(
                audio.getId(),
                audio.getTitle(),
                audio.getDescription(),
                audio.getDurationSeconds(),
                audio.getAudioUrl()
            );
        }
    }
}
