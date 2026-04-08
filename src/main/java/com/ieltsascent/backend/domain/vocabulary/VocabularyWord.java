package com.ieltsascent.backend.domain.vocabulary;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vocabulary_word")
@Getter
@Setter
public class VocabularyWord extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String word;

    @Column(nullable = false, length = 1000)
    private String simpleMeaning;

    @Column(length = 4000)
    private String detailedMeaning;

    @Column(nullable = false, length = 50)
    private String partOfSpeech;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VocabularyLevel level;

    @Column(nullable = false)
    private Double ieltsBandMin;

    @Column(nullable = false)
    private Double ieltsBandMax;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "vocabulary_word_tag", joinColumns = @JoinColumn(name = "vocabulary_word_id"))
    @Column(name = "tag", nullable = false, length = 100)
    private Set<String> tags = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "vocabulary_word_synonym", joinColumns = @JoinColumn(name = "vocabulary_word_id"))
    @Column(name = "value", nullable = false, length = 255)
    private Set<String> synonyms = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "vocabulary_word_antonym", joinColumns = @JoinColumn(name = "vocabulary_word_id"))
    @Column(name = "value", nullable = false, length = 255)
    private Set<String> antonyms = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "vocabulary_word_collocation", joinColumns = @JoinColumn(name = "vocabulary_word_id"))
    @Column(name = "value", nullable = false, length = 255)
    private Set<String> collocations = new HashSet<>();

    @Column(length = 2000)
    private String commonMistake;

    @Column(nullable = false)
    private Boolean premium = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VocabularyStatus status = VocabularyStatus.DRAFT;

    @OneToMany(mappedBy = "vocabularyWord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VocabularyExample> examples = new ArrayList<>();

    public void addExample(VocabularyExample example) {
        examples.add(example);
        example.setVocabularyWord(this);
    }

    public void removeExample(VocabularyExample example) {
        examples.remove(example);
        example.setVocabularyWord(null);
    }
}
