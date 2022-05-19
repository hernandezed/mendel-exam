package com.edh.mendelexam.api.dtos.responses;

public class TransactionResponseDto {
    private final Long id;
    private final String type;
    private final double amount;

    public TransactionResponseDto(Long id, String type, double amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
