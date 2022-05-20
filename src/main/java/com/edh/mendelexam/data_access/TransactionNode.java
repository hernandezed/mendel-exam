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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionNode that = (TransactionNode) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return childs != null ? childs.equals(that.childs) : that.childs == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (childs != null ? childs.hashCode() : 0);
        return result;
    }
}
