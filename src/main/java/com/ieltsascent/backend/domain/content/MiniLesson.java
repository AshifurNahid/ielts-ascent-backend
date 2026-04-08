package com.ieltsascent.backend.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MiniLesson extends ContentItem {
    @Column(length = 4000)
    private String lessonBody;
}
