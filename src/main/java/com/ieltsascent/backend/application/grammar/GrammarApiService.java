package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrammarApiService {
    private final GrammarUserService grammarUserService;
    private final UserGrammarProfileService userGrammarProfileService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public List<GrammarDtos.TopicResponse> topics() {
        return topics(currentUserProvider.userId());
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.TopicResponse> topics(Long userId) {
        return grammarUserService.getTopics(userId).stream().map(GrammarMapper::toTopicResponse).toList();
    }

    @Transactional(readOnly = true)
    public GrammarDtos.TopicResponse topic(Long userId, Long topicId) {
        return GrammarMapper.toTopicResponse(grammarUserService.getTopic(userId, topicId));
    }

    @Transactional(readOnly = true)
    public GrammarDtos.TopicResponse topic(Long topicId) {
        return topic(currentUserProvider.userId(), topicId);
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.QuestionResponse> topicQuestions(Long userId, Long topicId) {
        return grammarUserService.getTopicQuestions(userId, topicId).stream().map(GrammarMapper::toQuestionResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.QuestionResponse> topicQuestions(Long topicId) {
        return topicQuestions(currentUserProvider.userId(), topicId);
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.DrillTemplateResponse> drills() {
        return grammarUserService.activeDrills().stream().map(GrammarMapper::toDrillTemplateResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.QuestionResponse> drillQuestions(Long userId, GrammarQuestionType type, int limit) {
        int cappedLimit = Math.min(limit, 50);
        return grammarUserService.getDrillQuestions(userId, type, cappedLimit).stream().map(GrammarMapper::toQuestionResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.QuestionResponse> drillQuestions(GrammarQuestionType type, int limit) {
        return drillQuestions(currentUserProvider.userId(), type, limit);
    }

    @Transactional(readOnly = true)
    public GrammarDtos.RecommendationResponse recommendations(Long userId, int limit) {
        return grammarUserService.recommendations(userId, Math.min(limit, 20));
    }

    @Transactional(readOnly = true)
    public GrammarDtos.RecommendationResponse recommendations(int limit) {
        return recommendations(currentUserProvider.userId(), limit);
    }

    @Transactional(readOnly = true)
    public GrammarDtos.UserGrammarProfileResponse profile(Long userId) {
        return GrammarMapper.toProfileResponse(userGrammarProfileService.getOrCreate(userId));
    }

    @Transactional(readOnly = true)
    public GrammarDtos.UserGrammarProfileResponse profile() {
        return profile(currentUserProvider.userId());
    }

    @Transactional
    public GrammarDtos.UserGrammarProfileResponse upsertProfile(Long userId, GrammarDtos.UserGrammarProfileUpsertRequest request) {
        return GrammarMapper.toProfileResponse(userGrammarProfileService.upsert(userId, request));
    }

    @Transactional
    public GrammarDtos.UserGrammarProfileResponse upsertProfile(GrammarDtos.UserGrammarProfileUpsertRequest request) {
        return upsertProfile(currentUserProvider.userId(), request);
    }
}
