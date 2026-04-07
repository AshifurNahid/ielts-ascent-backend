package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.content.ListeningAudio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListeningAudioRepository extends JpaRepository<ListeningAudio, Long> {
}
