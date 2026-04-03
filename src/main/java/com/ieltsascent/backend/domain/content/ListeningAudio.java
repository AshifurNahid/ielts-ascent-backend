package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ListeningAudio extends ContentItem {
    private String audioUrl;
    private Integer durationSeconds;
}
