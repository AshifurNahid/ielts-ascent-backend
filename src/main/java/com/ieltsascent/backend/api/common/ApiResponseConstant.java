package com.ieltsascent.backend.api.common;

public final class ApiResponseConstant {
    private ApiResponseConstant() {}

    public static final String SUCCESS = "success";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";
    public static final String DELETED = "deleted";

    public static final String PROFILE_UPSERTED = "profile_upserted_successfully";
    public static final String USER_REGISTERED = "user_registered_successfully";
    public static final String LOGIN_SUCCESS = "login_successful";
    public static final String TOKEN_REFRESHED = "token_refreshed";

    public static final String SUBMISSION_STARTED = "submission_started";
    public static final String DRAFT_SAVED = "draft_saved";
    public static final String SUBMISSION_SUBMITTED = "submission_submitted";

    public static final String VALIDATION_FAILED = "validation_failed";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
}

