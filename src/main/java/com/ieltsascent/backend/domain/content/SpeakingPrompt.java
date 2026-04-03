package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SpeakingPrompt extends ContentItem {
    private String part;
}
