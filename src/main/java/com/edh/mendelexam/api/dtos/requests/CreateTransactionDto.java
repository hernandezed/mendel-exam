package com.edh.mendelexam.api.dtos.requests;

public class CreateTransactionDto {

    private final double amount;
    private final String type;

    public CreateTransactionDto(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}
