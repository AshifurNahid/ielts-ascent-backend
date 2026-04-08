package com.ieltsascent.backend.domain.content;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class ContentItem extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;
}
