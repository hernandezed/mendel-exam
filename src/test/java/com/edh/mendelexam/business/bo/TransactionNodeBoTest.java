package com.edh.mendelexam.business.bo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TransactionNodeBoTest {

    @Test
    void getTotal_withoutChilds_mustReturnAmountOfTransaction() {
        double amount = 100d;
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(amount, null);
        assertThat(transactionNodeBo.getTotal())
                .isEqualTo(amount);
    }

    @Test
    void getTotal_withChildsWithDepth1_mustReturnSumOfAmounts() {
        double totalAmount = 210d;
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(100d,
                List.of(new TransactionNodeBo(50d, null),
                        new TransactionNodeBo(60d, null)));

        assertThat(transactionNodeBo.getTotal()).isEqualTo(totalAmount);
    }

    @Test
    void getTotal_withChildsWithDepthN_mustReturnSumOfAmounts() {
        double totalAmount = 2510d;
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(100d,
                List.of(new TransactionNodeBo(50d,
                                List.of(new TransactionNodeBo(100d, null),
                                        new TransactionNodeBo(200d, List.of(
                                                new TransactionNodeBo(2000d, null)
                                        )))),
                        new TransactionNodeBo(60d, null)));

        assertThat(transactionNodeBo.getTotal()).isEqualTo(totalAmount);
    }
}
