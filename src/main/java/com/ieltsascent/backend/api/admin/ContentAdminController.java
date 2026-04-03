package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.content.ReadingPassage;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import com.ieltsascent.backend.domain.content.VocabularyItem;
import com.ieltsascent.backend.domain.content.WritingPrompt;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import com.ieltsascent.backend.infrastructure.persistence.ReadingPassageRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingPromptRepository;
import com.ieltsascent.backend.infrastructure.persistence.VocabularyItemRepository;
import com.ieltsascent.backend.infrastructure.persistence.WritingPromptRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
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
    private final ReadingPassageRepository readingPassageRepository;
    private final ListeningAudioRepository listeningAudioRepository;
    private final WritingPromptRepository writingPromptRepository;
    private final SpeakingPromptRepository speakingPromptRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    @PostMapping("/reading")
    public ApiResponse<ReadingResponse> createReading(@Valid @RequestBody ReadingRequest request) {
        ReadingPassage passage = new ReadingPassage();
        applyReadingRequest(passage, request);
        return ApiResponse.success(ReadingResponse.from(readingPassageRepository.save(passage)));
    }

    @GetMapping("/reading")
    public ApiResponse<PageResponse<ReadingResponse>> listReading(@ParameterObject Pageable pageable) {
        Page<ReadingResponse> page = readingPassageRepository.findAll(pageable)
            .map(ReadingResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/reading/{id}")
    public ApiResponse<ReadingResponse> getReading(@PathVariable UUID id) {
        return ApiResponse.success(ReadingResponse.from(findReading(id)));
    }

    @PutMapping("/reading/{id}")
    public ApiResponse<ReadingResponse> updateReading(
        @PathVariable UUID id,
        @Valid @RequestBody ReadingRequest request
    ) {
        ReadingPassage passage = findReading(id);
        applyReadingRequest(passage, request);
        return ApiResponse.success(ReadingResponse.from(readingPassageRepository.save(passage)));
    }

    @DeleteMapping("/reading/{id}")
    public ApiResponse<Void> deleteReading(@PathVariable UUID id) {
        readingPassageRepository.delete(findReading(id));
        return ApiResponse.success(null);
    }

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
    public ApiResponse<ListeningResponse> getListening(@PathVariable UUID id) {
        return ApiResponse.success(ListeningResponse.from(findListening(id)));
    }

    @PutMapping("/listening/{id}")
    public ApiResponse<ListeningResponse> updateListening(
        @PathVariable UUID id,
        @Valid @RequestBody ListeningRequest request
    ) {
        ListeningAudio audio = findListening(id);
        applyListeningRequest(audio, request);
        return ApiResponse.success(ListeningResponse.from(listeningAudioRepository.save(audio)));
    }

    @DeleteMapping("/listening/{id}")
    public ApiResponse<Void> deleteListening(@PathVariable UUID id) {
        listeningAudioRepository.delete(findListening(id));
        return ApiResponse.success(null);
    }

    @PostMapping("/writing-prompts")
    public ApiResponse<WritingPromptResponse> createWritingPrompt(@Valid @RequestBody WritingPromptRequest request) {
        WritingPrompt prompt = new WritingPrompt();
        applyWritingPromptRequest(prompt, request);
        return ApiResponse.success(WritingPromptResponse.from(writingPromptRepository.save(prompt)));
    }

    @GetMapping("/writing-prompts")
    public ApiResponse<PageResponse<WritingPromptResponse>> listWritingPrompts(@ParameterObject Pageable pageable) {
        Page<WritingPromptResponse> page = writingPromptRepository.findAll(pageable)
            .map(WritingPromptResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/writing-prompts/{id}")
    public ApiResponse<WritingPromptResponse> getWritingPrompt(@PathVariable UUID id) {
        return ApiResponse.success(WritingPromptResponse.from(findWritingPrompt(id)));
    }

    @PutMapping("/writing-prompts/{id}")
    public ApiResponse<WritingPromptResponse> updateWritingPrompt(
        @PathVariable UUID id,
        @Valid @RequestBody WritingPromptRequest request
    ) {
        WritingPrompt prompt = findWritingPrompt(id);
        applyWritingPromptRequest(prompt, request);
        return ApiResponse.success(WritingPromptResponse.from(writingPromptRepository.save(prompt)));
    }

    @DeleteMapping("/writing-prompts/{id}")
    public ApiResponse<Void> deleteWritingPrompt(@PathVariable UUID id) {
        writingPromptRepository.delete(findWritingPrompt(id));
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
    public ApiResponse<SpeakingPromptResponse> getSpeakingPrompt(@PathVariable UUID id) {
        return ApiResponse.success(SpeakingPromptResponse.from(findSpeakingPrompt(id)));
    }

    @PutMapping("/speaking-prompts/{id}")
    public ApiResponse<SpeakingPromptResponse> updateSpeakingPrompt(
        @PathVariable UUID id,
        @Valid @RequestBody SpeakingPromptRequest request
    ) {
        SpeakingPrompt prompt = findSpeakingPrompt(id);
        applySpeakingPromptRequest(prompt, request);
        return ApiResponse.success(SpeakingPromptResponse.from(speakingPromptRepository.save(prompt)));
    }

    @DeleteMapping("/speaking-prompts/{id}")
    public ApiResponse<Void> deleteSpeakingPrompt(@PathVariable UUID id) {
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
    public ApiResponse<VocabularyResponse> getVocabulary(@PathVariable UUID id) {
        return ApiResponse.success(VocabularyResponse.from(findVocabulary(id)));
    }

    @PutMapping("/vocabulary/{id}")
    public ApiResponse<VocabularyResponse> updateVocabulary(
        @PathVariable UUID id,
        @Valid @RequestBody VocabularyRequest request
    ) {
        VocabularyItem item = findVocabulary(id);
        applyVocabularyRequest(item, request);
        return ApiResponse.success(VocabularyResponse.from(vocabularyItemRepository.save(item)));
    }

    @DeleteMapping("/vocabulary/{id}")
    public ApiResponse<Void> deleteVocabulary(@PathVariable UUID id) {
        vocabularyItemRepository.delete(findVocabulary(id));
        return ApiResponse.success(null);
    }

    private ReadingPassage findReading(UUID id) {
        return readingPassageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reading passage not found"));
    }

    private ListeningAudio findListening(UUID id) {
        return listeningAudioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Listening audio not found"));
    }

    private WritingPrompt findWritingPrompt(UUID id) {
        return writingPromptRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Writing prompt not found"));
    }

    private SpeakingPrompt findSpeakingPrompt(UUID id) {
        return speakingPromptRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Speaking prompt not found"));
    }

    private VocabularyItem findVocabulary(UUID id) {
        return vocabularyItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary item not found"));
    }

    private static void applyReadingRequest(ReadingPassage passage, ReadingRequest request) {
        passage.setTitle(request.title());
        passage.setDescription(request.description());
        passage.setPassageText(request.passageText());
        passage.setDifficulty(request.difficulty());
    }

    private static void applyListeningRequest(ListeningAudio audio, ListeningRequest request) {
        audio.setTitle(request.title());
        audio.setDescription(request.description());
        audio.setAudioUrl(request.audioUrl());
        audio.setDurationSeconds(request.durationSeconds());
    }

    private static void applyWritingPromptRequest(WritingPrompt prompt, WritingPromptRequest request) {
        prompt.setTitle(request.title());
        prompt.setDescription(request.description());
        prompt.setTaskType(request.taskType());
        prompt.setCategory(request.category());
        prompt.setQuestionText(request.questionText());
        prompt.setChartType(request.chartType());
        prompt.setDifficulty(request.difficulty());
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

    public record ReadingRequest(
        @NotBlank String title,
        String description,
        @NotBlank String passageText,
        @NotBlank String difficulty
    ) {
    }

    public record ListeningRequest(
        @NotBlank String title,
        String description,
        @NotBlank String audioUrl,
        @NotNull Integer durationSeconds
    ) {
    }

    public record WritingPromptRequest(
        @NotBlank String title,
        String description,
        @NotBlank String taskType,
        String category,
        String questionText,
        String chartType,
        String difficulty
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

    public record ReadingResponse(
        UUID id,
        String title,
        String description,
        String passageText,
        String difficulty
    ) {
        public static ReadingResponse from(ReadingPassage passage) {
            return new ReadingResponse(
                passage.getId(),
                passage.getTitle(),
                passage.getDescription(),
                passage.getPassageText(),
                passage.getDifficulty()
            );
        }
    }

    public record ListeningResponse(
        UUID id,
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

    public record WritingPromptResponse(
        UUID id,
        String title,
        String description,
        String taskType,
        String category,
        String questionText,
        String chartType,
        String difficulty
    ) {
        public static WritingPromptResponse from(WritingPrompt prompt) {
            return new WritingPromptResponse(
                prompt.getId(),
                prompt.getTitle(),
                prompt.getDescription(),
                prompt.getTaskType(),
                prompt.getCategory(),
                prompt.getQuestionText(),
                prompt.getChartType(),
                prompt.getDifficulty()
            );
        }
    }

    public record SpeakingPromptResponse(
        UUID id,
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
        UUID id,
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
