package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WritingPrompt extends ContentItem {
    private String taskType;
    private String category;
    private String questionText;
    private String chartType;
    private String difficulty;
}
