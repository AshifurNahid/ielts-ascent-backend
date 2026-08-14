package com.ieltsascent.backend.application.practice.dto;

import com.ieltsascent.backend.domain.content.ListeningAudio;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PracticeListeningTaskDto {
    private final Long id;
    private final String title;
    private final String description;
    private final Integer durationSeconds;
    private final String audioUrl;

    public static PracticeListeningTaskDto from(ListeningAudio audio) {
        return new PracticeListeningTaskDto(
            audio.getId(),
            audio.getTitle(),
            audio.getDescription(),
            audio.getDurationSeconds(),
            audio.getAudioUrl()
        );
    }
}

