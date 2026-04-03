package com.ieltsascent.backend.domain.writing;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.infrastructure.persistence.StringListJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserWritingProfile extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    private Double currentEstimatedWritingBand;
    private Double targetWritingBand;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> weakCategories = new ArrayList<>();

    private Instant lastSubmissionAt;
}
