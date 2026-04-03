package com.ieltsascent.backend.api.common;

public record ApiResponse<T>(T data, String message) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, "success");
    }

    public static <T> ApiResponse<T> failure(T data) {
        return new ApiResponse<>(data, "error");
    }
}
