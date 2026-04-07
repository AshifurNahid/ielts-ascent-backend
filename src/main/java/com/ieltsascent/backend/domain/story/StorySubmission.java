package com.ieltsascent.backend.domain.story;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StorySubmission extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String partsJson;

    @Column(nullable = false)
    private Integer wordCount;
}
