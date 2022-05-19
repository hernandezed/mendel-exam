package com.edh.mendelexam.api.dtos.requests;

import com.edh.mendelexam.api.validators.AboveZero;

public class CreateTransactionDto {

    @AboveZero
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
