package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.content.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class PracticeAnswer extends BaseEntity {
    @ManyToOne
    private PracticeSession session;

    @ManyToOne
    private Question question;

    private String answer;

    private Boolean correct;

    public PracticeSession getSession() {
        return session;
    }

    public void setSession(PracticeSession session) {
        this.session = session;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
