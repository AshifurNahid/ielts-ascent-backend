package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.content.ReadingPassage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingPassageRepository extends JpaRepository<ReadingPassage, UUID> {
}
