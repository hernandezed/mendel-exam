package com.edh.mendelexam.business.bos;

import java.util.List;
import java.util.Set;

public class TransactionNodeBo {
    private final Long id;
    private final double amount;
    private final String type;
    private final Set<TransactionNodeBo> childs;

    public TransactionNodeBo(Long id, double amount, String type, Set<TransactionNodeBo> childs) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.childs = childs;
    }

    public double getTotal() {
        return amount + getChildTotal();
    }

    private double getChildTotal() {
        if (childs != null)
            return childs.stream().reduce(0d, (acc, node) -> acc + node.getTotal(), Double::sum);
        return 0;
    }

    @Override
    public String toString() {
        return "TransactionNodeBo{" +
                "id=" + id +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", childs=" + childs +
                '}';
    }
}
