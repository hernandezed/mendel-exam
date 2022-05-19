package com.edh.mendelexam.api.dtos.requests;

import com.edh.mendelexam.api.validators.AboveZero;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateTransactionDto {

    @AboveZero
    private final double amount;
    @NotEmpty
    private final String type;
    private final Long parentId;

    public CreateTransactionDto(double amount, String type, Long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public Long getParentId() {
        return parentId;
    }
}
