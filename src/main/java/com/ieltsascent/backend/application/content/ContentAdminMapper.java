package com.ieltsascent.backend.application.content;

import com.ieltsascent.backend.api.content.dto.AdminListeningRequest;
import com.ieltsascent.backend.api.content.dto.AdminListeningResponse;
import com.ieltsascent.backend.api.content.dto.AdminSpeakingPromptRequest;
import com.ieltsascent.backend.api.content.dto.AdminSpeakingPromptResponse;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;

public final class ContentAdminMapper {
    private ContentAdminMapper() {
    }

    public static AdminListeningResponse toListeningResponse(ListeningAudio audio) {
        return new AdminListeningResponse(
            audio.getId(),
            audio.getTitle(),
            audio.getDescription(),
            audio.getAudioUrl(),
            audio.getDurationSeconds()
        );
    }

    public static AdminSpeakingPromptResponse toSpeakingPromptResponse(SpeakingPrompt prompt) {
        return new AdminSpeakingPromptResponse(
            prompt.getId(),
            prompt.getTitle(),
            prompt.getDescription(),
            prompt.getPart()
        );
    }

    public static void applyListeningRequest(ListeningAudio audio, AdminListeningRequest request) {
        audio.setTitle(request.getTitle());
        audio.setDescription(request.getDescription());
        audio.setAudioUrl(request.getAudioUrl());
        audio.setDurationSeconds(request.getDurationSeconds());
    }

    public static void applySpeakingPromptRequest(SpeakingPrompt prompt, AdminSpeakingPromptRequest request) {
        prompt.setTitle(request.getTitle());
        prompt.setDescription(request.getDescription());
        prompt.setPart(request.getPart());
    }
}

