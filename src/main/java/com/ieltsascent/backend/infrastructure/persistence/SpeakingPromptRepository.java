package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakingPromptRepository extends JpaRepository<SpeakingPrompt, Long> {
}
