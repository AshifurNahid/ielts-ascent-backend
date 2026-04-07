package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.mocktest.MockTestSectionResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockTestSectionResultRepository extends JpaRepository<MockTestSectionResult, Long> {
    List<MockTestSectionResult> findTop20BySessionUserIdAndSectionIgnoreCaseOrderByCreatedAtDesc(Long userId, String section);
}
