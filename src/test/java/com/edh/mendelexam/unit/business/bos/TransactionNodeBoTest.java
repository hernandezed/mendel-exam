package com.edh.mendelexam.unit.business.bos;

import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TransactionNodeBoTest {

    @Test
    void getTotal_withoutChilds_mustReturnAmountOfTransaction() {
        double amount = 100d;
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(1L, amount, "type", null);
        assertThat(transactionNodeBo.getTotal())
                .isEqualTo(amount);
    }

    @Test
    void getTotal_withChildsWithDepth1_mustReturnSumOfAmounts() {
        double totalAmount = 210d;
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(1L, 100d,
                "type", Sets.newHashSet(new TransactionNodeBo(2L, 50d, "type", null),
                new TransactionNodeBo(3L, 60d, "type", null)));

        assertThat(transactionNodeBo.getTotal()).isEqualTo(totalAmount);
    }

    @Test
    void getTotal_withChildsWithDepthN_mustReturnSumOfAmounts() {
        double totalAmount = 2510d;
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(1L, 100d,
                "type", Sets.newHashSet(new TransactionNodeBo(2L, 50d,
                        "type", Sets.newHashSet(new TransactionNodeBo(3L, 100d, "type", null),
                                        new TransactionNodeBo(4L, 200d, "type", Sets.newHashSet(
                                                new TransactionNodeBo(5L, 2000d, "type", null)
                                        )))),
                        new TransactionNodeBo(6L, 60d, "type", null)));

        assertThat(transactionNodeBo.getTotal()).isEqualTo(totalAmount);
    }
}
