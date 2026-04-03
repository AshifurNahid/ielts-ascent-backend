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
public class MiniLesson extends ContentItem {
    @Column(length = 4000)
    private String lessonBody;
}
