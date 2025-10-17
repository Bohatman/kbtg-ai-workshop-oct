package com.example.swagger.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {

    @Schema(
        description = "Response status",
        example = "success",
        allowableValues = {"success", "error"}
    )
    private String status;

    @Schema(
        description = "Response message",
        example = "Operation completed successfully"
    )
    private String message;

    @Schema(description = "Response data")
    private T data;

    // Constructors
    public ApiResponse() {}

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}