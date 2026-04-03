package com.ieltsascent.backend.api.writing;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WritingControllerTest {
    @Test
    void rejectsEvaluationAccessFromOtherUser() {
        WritingService writingService = mock(WritingService.class);
        WritingController controller = new WritingController(writingService);

        UUID requesterId = UUID.randomUUID();
        UUID submissionId = UUID.randomUUID();

        when(writingService.evaluation(requesterId, submissionId))
            .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden"));

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(requesterId.toString(), null);

        assertThatThrownBy(() -> controller.evaluation(auth, submissionId))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Forbidden");
    }
}
