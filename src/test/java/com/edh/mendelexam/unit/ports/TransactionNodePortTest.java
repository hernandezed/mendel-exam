package com.edh.mendelexam.unit.ports;

import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;
import com.edh.mendelexam.ports.TransactionNodePort;
import com.edh.mendelexam.unit.ports.impl.TransactionNodePortImpl;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionNodePortTest {

    TransactionNodeRepository transactionNodeRepository = mock(TransactionNodeRepository.class);
    TransactionNodePort transactionNodePort = new TransactionNodePortImpl(transactionNodeRepository);

    @Test
    void findById_whenExistsTransactionAndHasNotChilds_mustReturnTransaction() {
        long transactionId = 1L;
        when(transactionNodeRepository.findById(transactionId)).thenReturn(Optional.of(new TransactionNode(transactionId, 20d, null, "type")));

        Optional<TransactionNodeBo> transaction = transactionNodePort.findById(transactionId);
        TransactionNodeBo expectedTransaction = new TransactionNodeBo(transactionId, 20d, "type", Sets.newHashSet());
        assertThat(transaction.get()).usingRecursiveComparison().isEqualTo(expectedTransaction);
    }

    @Test
    void findById_whenNonExistsTransaction_mustReturnEmptyOptional() {
        long transactionId = 1L;
        when(transactionNodeRepository.findById(transactionId)).thenReturn(Optional.empty());
        Optional<TransactionNodeBo> transaction = transactionNodePort.findById(transactionId);
        assertThat(transaction).isEmpty();
    }

    @Test
    void findById_whenExistsTransactionAndHasChilds_mustReturnTransactionWithChilds() {
        long transactionId = 1L;
        TransactionNode mockRootTransaction = new TransactionNode(transactionId, 20d, null, "type");
        TransactionNode firstChild = new TransactionNode(2L, 30d, transactionId, "type2");
        mockRootTransaction.addChild(firstChild);
        TransactionNode secondChild = new TransactionNode(3L, 30d, 2L, "type2");
        firstChild.addChild(secondChild);

        when(transactionNodeRepository.findById(transactionId)).thenReturn(Optional.of(mockRootTransaction));

        Optional<TransactionNodeBo> transaction = transactionNodePort.findById(transactionId);
        TransactionNodeBo expectedTransaction = new TransactionNodeBo(transactionId, 20d, "type",
                Sets.newHashSet(new TransactionNodeBo(2L, 30d, "type2",
                        Sets.newHashSet(new TransactionNodeBo(3L, 30d, "type2", Sets.newHashSet())))));
        assertThat(transaction.get()).usingRecursiveComparison().isEqualTo(expectedTransaction);
    }
}
