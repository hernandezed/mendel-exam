package com.edh.mendelexam.api.dtos.responses;

public class TransactionResponseDto {
    private final String status;
    private final String message;

    private TransactionResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static TransactionResponseDto ok() {
        return new TransactionResponseDto("ok", null);
    }

    public static TransactionResponseDto error(String message) {
        return new TransactionResponseDto("error", message);
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
