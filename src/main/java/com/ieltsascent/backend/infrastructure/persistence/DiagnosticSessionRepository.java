package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.assessment.DiagnosticSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticSessionRepository extends JpaRepository<DiagnosticSession, Long> {
}
