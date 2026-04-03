package com.ieltsascent.backend.application.progress;

public final class ProgressEnums {
    private ProgressEnums() {}

    public enum SkillType {
        LISTENING,
        READING,
        WRITING,
        SPEAKING
    }

    public enum ConfidenceLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum ModuleType {
        READING,
        LISTENING,
        WRITING,
        SPEAKING,
        GRAMMAR,
        VOCABULARY
    }
}
