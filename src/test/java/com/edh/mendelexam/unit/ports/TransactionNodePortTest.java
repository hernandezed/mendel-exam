package com.edh.mendelexam.unit.ports;

import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.exception.NoSuchParentException;
import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;
import com.edh.mendelexam.ports.TransactionNodePort;
import com.edh.mendelexam.ports.impl.TransactionNodePortImpl;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
        verify(transactionNodeRepository).findById(transactionId);
    }

    @Test
    void findById_whenNonExistsTransaction_mustReturnEmptyOptional() {
        long transactionId = 1L;
        when(transactionNodeRepository.findById(transactionId)).thenReturn(Optional.empty());
        Optional<TransactionNodeBo> transaction = transactionNodePort.findById(transactionId);
        assertThat(transaction).isEmpty();
        verify(transactionNodeRepository).findById(transactionId);
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
        verify(transactionNodeRepository).findById(transactionId);
    }

    @Test
    void getIdByType_mustCallRepository() {
        String type = "type";
        HashSet<Long> transactionIds = Sets.newHashSet(1L, 2L, 3L);
        when(transactionNodeRepository.getIdByType(type)).thenReturn(transactionIds);

        Set<Long> resultTransactionIds = transactionNodePort.getIdByType(type);

        assertThat(resultTransactionIds).containsAll(transactionIds);
        verify(transactionNodeRepository).getIdByType(type);
    }

    @Test
    void save_withTransactionPersistedWithChilds_mustCallRepositoryWithEntity() {
        CreateTransactionBo createTransactionBo = new CreateTransactionBo(1L, 20d, null, "type");
        TransactionNode transactionEntity = new TransactionNode(1L, 20d, null, "type");
        TransactionNode mockResultTransactionEntity = new TransactionNode(1L, 20d, null, "type");
        mockResultTransactionEntity.addChild(new TransactionNode(2L, 30d, null, "type"));

        when(transactionNodeRepository.save(transactionEntity)).thenReturn(mockResultTransactionEntity);

        TransactionNodeBo resultTransaction = transactionNodePort.save(createTransactionBo);
        TransactionNodeBo expectedTransactionBo = new TransactionNodeBo(1L, 20d, "type",
                Sets.newHashSet(new TransactionNodeBo(2L, 30d, "type", Sets.newHashSet())));
        assertThat(resultTransaction).usingRecursiveComparison().isEqualTo(expectedTransactionBo);
        verify(transactionNodeRepository).save(transactionEntity);
    }

    @Test
    void save_withInvalidParentId_mustThrowBusinessException() {
        CreateTransactionBo createTransactionBo = new CreateTransactionBo(1L, 20d, 30L, "type");
        TransactionNode transactionEntity = new TransactionNode(1L, 20d, 30L, "type");
        when(transactionNodeRepository.save(transactionEntity)).thenThrow(IllegalStateException.class);

        assertThatThrownBy(() -> transactionNodePort.save(createTransactionBo)).isInstanceOf(NoSuchParentException.class)
                .hasCauseExactlyInstanceOf(IllegalStateException.class);

    }

    @Test
    void exists_whenTransactionExists_mustCallRepositoryExistsAndReturnTrue() {
        when(transactionNodeRepository.exists(1L)).thenReturn(true);

        assertThat(transactionNodePort.exists(1L)).isTrue();
        verify(transactionNodeRepository).exists(1L);
    }

    @Test
    void exists_whenTransactionNoExists_mustCallRepositoryExistsAndReturnFalse() {
        when(transactionNodeRepository.exists(1L)).thenReturn(false);

        assertThat(transactionNodePort.exists(1L)).isFalse();
        verify(transactionNodeRepository).exists(1L);
    }
}
