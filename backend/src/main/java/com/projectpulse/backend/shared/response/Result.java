package com.projectpulse.backend.shared.response;

public final class Result {

    private Result() {
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> success(String message) {
        return success(message, null);
    }

    public static <T> ApiResponse<T> failure(String message, T data) {
        return ApiResponse.<T>builder()
                .flag(false)
                .code(StatusCode.BAD_REQUEST)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> failure(String message) {
        return failure(message, null);
    }
}
