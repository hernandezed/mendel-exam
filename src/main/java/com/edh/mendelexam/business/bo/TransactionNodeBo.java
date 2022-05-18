package com.edh.mendelexam.business.bo;

import java.util.List;

public class TransactionNodeBo {
    private double amount;
    private List<TransactionNodeBo> childs;

    public TransactionNodeBo(double amount, List<TransactionNodeBo> childs) {
        this.amount = amount;
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
}
