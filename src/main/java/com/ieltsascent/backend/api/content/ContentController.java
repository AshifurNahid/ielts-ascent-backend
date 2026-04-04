package com.ieltsascent.backend.api.content;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.application.content.ContentService;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.content.VocabularyItem;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/listening")
    public ApiResponse<PageResponse<ListeningAudioDto>> listening(@ParameterObject Pageable pageable) {
        Page<ListeningAudioDto> page = contentService.listListeningAudios(pageable)
            .map(ListeningAudioDto::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/vocabulary")
    public ApiResponse<PageResponse<VocabularyItemDto>> vocabulary(@ParameterObject Pageable pageable) {
        Page<VocabularyItemDto> page = contentService.listVocabularyItems(pageable)
            .map(VocabularyItemDto::from);
        return ApiResponse.success(PageResponse.from(page));
    }


    public record ListeningAudioDto(UUID id, String title, String description, Integer durationSeconds, String audioUrl) {
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

    public record VocabularyItemDto(UUID id, String word, String definition, String example, String category) {
        public static VocabularyItemDto from(VocabularyItem item) {
            return new VocabularyItemDto(
                item.getId(),
                item.getTitle(),
                item.getDefinition(),
                item.getExample(),
                item.getCategory()
            );
        }
    }
}
