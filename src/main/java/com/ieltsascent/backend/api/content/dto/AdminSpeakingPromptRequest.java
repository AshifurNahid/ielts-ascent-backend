package com.ieltsascent.backend.api.content.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSpeakingPromptRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String part;
}

