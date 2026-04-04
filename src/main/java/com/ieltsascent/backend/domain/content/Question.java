package com.ieltsascent.backend.domain.content;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Question extends BaseEntity {
    @Column(nullable = false)
    private String prompt;

    private String type;

    @ManyToOne
    private ListeningAudio listeningAudio;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public ListeningAudio getListeningAudio() {
        return listeningAudio;
    }

    public void setListeningAudio(ListeningAudio listeningAudio) {
        this.listeningAudio = listeningAudio;
    }
}
