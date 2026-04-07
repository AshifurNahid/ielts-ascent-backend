package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import com.ieltsascent.backend.infrastructure.persistence.grammar.UserGrammarProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGrammarProfileService {
    private final UserGrammarProfileRepository repository;

    @Transactional
    public UserGrammarProfile getOrCreate(Long userId) {
        return repository.findByUserId(userId).orElseGet(() -> repository.save(defaultProfile(userId)));
    }

    @Transactional
    public UserGrammarProfile upsert(Long userId, GrammarDtos.UserGrammarProfileUpsertRequest request) {
        UserGrammarProfile profile = repository.findByUserId(userId).orElseGet(() -> defaultProfile(userId));
        profile.setEnglishLevel(request.englishLevel());
        profile.setCurrentIeltsBand(request.currentIeltsBand());
        profile.setTargetIeltsBand(request.targetIeltsBand());
        profile.setWeakTopics(GrammarMapper.normalizeStringSet(request.weakTopics()));
        profile.setWeakDrillTypes(request.weakDrillTypes() == null ? java.util.Set.of() : request.weakDrillTypes());
        profile.setPremiumUser(request.premiumUser());
        return repository.save(profile);
    }

    private UserGrammarProfile defaultProfile(Long userId) {
        UserGrammarProfile profile = new UserGrammarProfile();
        profile.setUserId(userId);
        profile.setTargetIeltsBand(6.5);
        return profile;
    }
}
