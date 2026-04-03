package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.mocktest.MockTestSectionResult;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockTestSectionResultRepository extends JpaRepository<MockTestSectionResult, UUID> {
    List<MockTestSectionResult> findTop20BySessionUserIdAndSectionIgnoreCaseOrderByCreatedAtDesc(UUID userId, String section);
}
