package com.ieltsascent.backend.api.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminListeningRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String audioUrl;

    @NotNull
    private Integer durationSeconds;
}

