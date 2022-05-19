package com.edh.mendelexam.data_access;

import java.util.HashSet;
import java.util.Set;

public class TransactionNode {
    private Long id;
    private Double amount;
    private Long parentId;
    private String type;
    private Set<TransactionNode> childs;

    public TransactionNode(Long id, Double amount, Long parentId, String type) {
        this.id = id;
        this.amount = amount;
        this.parentId = parentId;
        this.type = type;
        this.childs = new HashSet<>();
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setChilds(Set<TransactionNode> childs) {
        this.childs = childs;
    }

    public void addChild(TransactionNode transactionNode) {
        this.childs.add(transactionNode);
    }

    public Set<TransactionNode> getChilds() {
        return childs;
    }

    public void removeChild(TransactionNode transactionNode) {
        this.childs.remove(transactionNode);
    }
}
