package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpeakingPrompt extends ContentItem {
    private String part;
}
