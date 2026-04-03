package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.ai.AiInteractionLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiInteractionLogRepository extends JpaRepository<AiInteractionLog, UUID> {
}
