package com.ieltsascent.backend.application.auth;

public record ProfileReadiness(
    boolean readyForPersonalizedSuggestions,
    boolean hasDiagnosticAssessment,
    boolean hasBaselineBands,
    String nextRequiredAction,
    String guidanceMessage
) {
    public static ProfileReadiness ready(boolean hasDiagnosticAssessment, boolean hasBaselineBands) {
        return new ProfileReadiness(true, hasDiagnosticAssessment, hasBaselineBands, "NONE", "Profile is ready.");
    }

    public static ProfileReadiness requiresSetup(boolean hasDiagnosticAssessment, boolean hasBaselineBands) {
        return new ProfileReadiness(
            false,
            hasDiagnosticAssessment,
            hasBaselineBands,
            "COMPLETE_DIAGNOSTIC_OR_BASELINE",
            "Complete a diagnostic assessment or set baseline/current band and target band before requesting personalized suggestions."
        );
    }
}
