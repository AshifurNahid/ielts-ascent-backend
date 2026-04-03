package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "vocabulary_item"
)
public class VocabularyItem extends ContentItem {
    private String definition;
    private String example;
    private String category;
}
