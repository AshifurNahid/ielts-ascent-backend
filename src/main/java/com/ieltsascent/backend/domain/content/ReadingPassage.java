package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReadingPassage extends ContentItem {
    @Column(length = 8000)
    private String passageText;

    private String difficulty;
}
