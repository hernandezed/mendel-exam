package com.edh.mendelexam.business.bos;

import com.edh.mendelexam.data_access.TransactionNode;

import java.util.Set;

public class CreateTransactionBo {
    private final Long id;
    private final Double amount;
    private final Long parentId;
    private final String type;

    public CreateTransactionBo(Long id, Double amount, Long parentId, String type) {
        this.id = id;
        this.amount = amount;
        this.parentId = parentId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getType() {
        return type;
    }
}
