package com.ieltsascent.backend.api.content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSpeakingPromptResponse {
    private Long id;
    private String title;
    private String description;
    private String part;
}

