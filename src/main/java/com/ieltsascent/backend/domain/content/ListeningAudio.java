package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ListeningAudio extends ContentItem {
    private String audioUrl;
    private Integer durationSeconds;
}
