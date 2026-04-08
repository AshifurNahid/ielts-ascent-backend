package com.ieltsascent.backend.domain.ai;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AiInteractionLog extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    private String correlationId;
    private String provider;
    private String model;
    private String promptName;
    private String promptVersion;
    private Integer latencyMs;
    private Integer tokenUsage;

    @Column(columnDefinition = "TEXT")
    private String requestRedacted;

    @Column(columnDefinition = "TEXT")
    private String responseRedacted;
}
