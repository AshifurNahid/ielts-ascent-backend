package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import com.ieltsascent.backend.domain.content.VocabularyItem;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingPromptRepository;
import com.ieltsascent.backend.infrastructure.persistence.VocabularyItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ContentAdminController {
    private final ListeningAudioRepository listeningAudioRepository;
    private final SpeakingPromptRepository speakingPromptRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    @PostMapping("/listening")
    public ApiResponse<ListeningResponse> createListening(@Valid @RequestBody ListeningRequest request) {
        ListeningAudio audio = new ListeningAudio();
        applyListeningRequest(audio, request);
        return ApiResponse.success(ListeningResponse.from(listeningAudioRepository.save(audio)));
    }

    @GetMapping("/listening")
    public ApiResponse<PageResponse<ListeningResponse>> listListening(@ParameterObject Pageable pageable) {
        Page<ListeningResponse> page = listeningAudioRepository.findAll(pageable)
            .map(ListeningResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/listening/{id}")
    public ApiResponse<ListeningResponse> getListening(@PathVariable Long id) {
        return ApiResponse.success(ListeningResponse.from(findListening(id)));
    }

    @PutMapping("/listening/{id}")
    public ApiResponse<ListeningResponse> updateListening(
        @PathVariable Long id,
        @Valid @RequestBody ListeningRequest request
    ) {
        ListeningAudio audio = findListening(id);
        applyListeningRequest(audio, request);
        return ApiResponse.success(ListeningResponse.from(listeningAudioRepository.save(audio)));
    }

    @DeleteMapping("/listening/{id}")
    public ApiResponse<Void> deleteListening(@PathVariable Long id) {
        listeningAudioRepository.delete(findListening(id));
        return ApiResponse.success(null);
    }

    @PostMapping("/speaking-prompts")
    public ApiResponse<SpeakingPromptResponse> createSpeakingPrompt(@Valid @RequestBody SpeakingPromptRequest request) {
        SpeakingPrompt prompt = new SpeakingPrompt();
        applySpeakingPromptRequest(prompt, request);
        return ApiResponse.success(SpeakingPromptResponse.from(speakingPromptRepository.save(prompt)));
    }

    @GetMapping("/speaking-prompts")
    public ApiResponse<PageResponse<SpeakingPromptResponse>> listSpeakingPrompts(@ParameterObject Pageable pageable) {
        Page<SpeakingPromptResponse> page = speakingPromptRepository.findAll(pageable)
            .map(SpeakingPromptResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/speaking-prompts/{id}")
    public ApiResponse<SpeakingPromptResponse> getSpeakingPrompt(@PathVariable Long id) {
        return ApiResponse.success(SpeakingPromptResponse.from(findSpeakingPrompt(id)));
    }

    @PutMapping("/speaking-prompts/{id}")
    public ApiResponse<SpeakingPromptResponse> updateSpeakingPrompt(
        @PathVariable Long id,
        @Valid @RequestBody SpeakingPromptRequest request
    ) {
        SpeakingPrompt prompt = findSpeakingPrompt(id);
        applySpeakingPromptRequest(prompt, request);
        return ApiResponse.success(SpeakingPromptResponse.from(speakingPromptRepository.save(prompt)));
    }

    @DeleteMapping("/speaking-prompts/{id}")
    public ApiResponse<Void> deleteSpeakingPrompt(@PathVariable Long id) {
        speakingPromptRepository.delete(findSpeakingPrompt(id));
        return ApiResponse.success(null);
    }

    @PostMapping("/vocabulary")
    public ApiResponse<VocabularyResponse> createVocabulary(@Valid @RequestBody VocabularyRequest request) {
        VocabularyItem item = new VocabularyItem();
        applyVocabularyRequest(item, request);
        return ApiResponse.success(VocabularyResponse.from(vocabularyItemRepository.save(item)));
    }

    @GetMapping("/vocabulary")
    public ApiResponse<PageResponse<VocabularyResponse>> listVocabulary(@ParameterObject Pageable pageable) {
        Page<VocabularyResponse> page = vocabularyItemRepository.findAll(pageable)
            .map(VocabularyResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/vocabulary/{id}")
    public ApiResponse<VocabularyResponse> getVocabulary(@PathVariable Long id) {
        return ApiResponse.success(VocabularyResponse.from(findVocabulary(id)));
    }

    @PutMapping("/vocabulary/{id}")
    public ApiResponse<VocabularyResponse> updateVocabulary(
        @PathVariable Long id,
        @Valid @RequestBody VocabularyRequest request
    ) {
        VocabularyItem item = findVocabulary(id);
        applyVocabularyRequest(item, request);
        return ApiResponse.success(VocabularyResponse.from(vocabularyItemRepository.save(item)));
    }

    @DeleteMapping("/vocabulary/{id}")
    public ApiResponse<Void> deleteVocabulary(@PathVariable Long id) {
        vocabularyItemRepository.delete(findVocabulary(id));
        return ApiResponse.success(null);
    }

    private ListeningAudio findListening(Long id) {
        return listeningAudioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Listening audio not found"));
    }

    private SpeakingPrompt findSpeakingPrompt(Long id) {
        return speakingPromptRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Speaking prompt not found"));
    }

    private VocabularyItem findVocabulary(Long id) {
        return vocabularyItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary item not found"));
    }

    private static void applyListeningRequest(ListeningAudio audio, ListeningRequest request) {
        audio.setTitle(request.title());
        audio.setDescription(request.description());
        audio.setAudioUrl(request.audioUrl());
        audio.setDurationSeconds(request.durationSeconds());
    }

    private static void applySpeakingPromptRequest(SpeakingPrompt prompt, SpeakingPromptRequest request) {
        prompt.setTitle(request.title());
        prompt.setDescription(request.description());
        prompt.setPart(request.part());
    }

    private static void applyVocabularyRequest(VocabularyItem item, VocabularyRequest request) {
        item.setTitle(request.word());
        item.setDefinition(request.definition());
        item.setExample(request.example());
        item.setCategory(request.category());
    }

    public record ListeningRequest(
        @NotBlank String title,
        String description,
        @NotBlank String audioUrl,
        @NotNull Integer durationSeconds
    ) {
    }

    public record SpeakingPromptRequest(
        @NotBlank String title,
        String description,
        @NotBlank String part
    ) {
    }

    public record VocabularyRequest(
        @NotBlank String word,
        @NotBlank String definition,
        String example,
        @NotBlank String category
    ) {
    }


    public record ListeningResponse(
        Long id,
        String title,
        String description,
        String audioUrl,
        Integer durationSeconds
    ) {
        public static ListeningResponse from(ListeningAudio audio) {
            return new ListeningResponse(
                audio.getId(),
                audio.getTitle(),
                audio.getDescription(),
                audio.getAudioUrl(),
                audio.getDurationSeconds()
            );
        }
    }


    public record SpeakingPromptResponse(
        Long id,
        String title,
        String description,
        String part
    ) {
        public static SpeakingPromptResponse from(SpeakingPrompt prompt) {
            return new SpeakingPromptResponse(
                prompt.getId(),
                prompt.getTitle(),
                prompt.getDescription(),
                prompt.getPart()
            );
        }
    }

    public record VocabularyResponse(
        Long id,
        String word,
        String definition,
        String example,
        String category
    ) {
        public static VocabularyResponse from(VocabularyItem item) {
            return new VocabularyResponse(
                item.getId(),
                item.getTitle(),
                item.getDefinition(),
                item.getExample(),
                item.getCategory()
            );
        }
    }

}
