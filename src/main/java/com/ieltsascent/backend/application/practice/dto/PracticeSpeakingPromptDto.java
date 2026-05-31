package com.ieltsascent.backend.application.practice.dto;

import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PracticeSpeakingPromptDto {
    private final Long id;
    private final String title;
    private final String description;
    private final String part;

    public static PracticeSpeakingPromptDto from(SpeakingPrompt prompt) {
        return new PracticeSpeakingPromptDto(prompt.getId(), prompt.getTitle(), prompt.getDescription(), prompt.getPart());
    }
}

