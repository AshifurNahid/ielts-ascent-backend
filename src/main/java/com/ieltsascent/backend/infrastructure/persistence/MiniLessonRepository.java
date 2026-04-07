package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.content.MiniLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiniLessonRepository extends JpaRepository<MiniLesson, Long> {
}
