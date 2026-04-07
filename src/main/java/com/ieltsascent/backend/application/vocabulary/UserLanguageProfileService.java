package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.domain.auth.UserProfile;
import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import com.ieltsascent.backend.domain.vocabulary.UserLanguageProfile;
import com.ieltsascent.backend.infrastructure.persistence.UserProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.UserLanguageProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLanguageProfileService {
    private final UserLanguageProfileRepository userLanguageProfileRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserLanguageProfile getOrCreate(Long userId) {
        return userLanguageProfileRepository.findByUserId(userId)
            .orElseGet(() -> createDefault(userId));
    }

    @Transactional
    public UserLanguageProfile upsert(Long userId, VocabularyDtos.UserLanguageProfileUpsertRequest request) {
        UserLanguageProfile profile = userLanguageProfileRepository.findByUserId(userId)
            .orElseGet(() -> {
                UserLanguageProfile created = createDefault(userId);
                created.setUserId(userId);
                return created;
            });

        profile.setEnglishLevel(request.englishLevel());
        profile.setCurrentIeltsBand(request.currentIeltsBand());
        profile.setTargetIeltsBand(request.targetIeltsBand());
        profile.setWeakTags(VocabularyMapper.normalizedSet(request.weakTags()));
        profile.setPremiumUser(request.premiumUser());
        return userLanguageProfileRepository.save(profile);
    }

    private UserLanguageProfile createDefault(Long userId) {
        UserLanguageProfile profile = new UserLanguageProfile();
        profile.setUserId(userId);
        profile.setEnglishLevel(EnglishLevel.BEGINNER);
        userProfileRepository.findByUserId(userId)
            .ifPresent(coreProfile -> applyFromCoreProfile(profile, coreProfile));
        return userLanguageProfileRepository.save(profile);
    }

    private void applyFromCoreProfile(UserLanguageProfile profile, UserProfile coreProfile) {
        if (coreProfile.getCurrentBand() != null) {
            profile.setCurrentIeltsBand(coreProfile.getCurrentBand());
        }
        if (coreProfile.getTargetBand() != null) {
            profile.setTargetIeltsBand(coreProfile.getTargetBand());
        }
    }
}
