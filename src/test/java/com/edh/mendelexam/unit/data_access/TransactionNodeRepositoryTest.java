package com.edh.mendelexam.unit.data_access;

import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;
import com.edh.mendelexam.data_access.impl.TransactionNodeRepositoryImpl;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class TransactionNodeRepositoryTest {

    private final Map<Long, TransactionNode> transactions = new HashMap<>();
    private final Map<String, Set<Long>> transactionsTypes = new HashMap<>();
    private final TransactionNodeRepository transactionNodeRepository = new TransactionNodeRepositoryImpl(transactions, transactionsTypes);

    @AfterEach
    void tearDown() {
        transactions.clear();
        transactionsTypes.clear();
    }

    @Test
    void save_whenMapsAreEmptyAndNotHaveParent_thenAddTransactionNodeAndCategory() {
        Long transactionId = 1L;
        TransactionNode transactionNode = new TransactionNode(transactionId, 20d, null, "type");
        TransactionNode saved = transactionNodeRepository.save(transactionNode);

        assertThat(saved).isNotNull().isEqualTo(transactionNode);
        assertThat(transactions.get(transactionId)).isEqualTo(transactionNode);
        assertThat(transactionsTypes.get("type")).containsExactly(transactionId);
    }

    @Test
    void save_whenTransactionExists_mustUpdateTransaction() {
        Long transactionId = 2L;
        TransactionNode transactionNode = new TransactionNode(transactionId, 20d, null, "type");
        transactions.put(transactionId, new TransactionNode(transactionId, 40d, null, "type"));
        transactionsTypes.put("type", Sets.newHashSet(transactionId));

        TransactionNode saved = transactionNodeRepository.save(transactionNode);

        assertThat(saved).isNotNull().isEqualTo(transactionNode);
        assertThat(transactions.get(transactionId)).isEqualTo(transactionNode);
        assertThat(transactionsTypes.get("type")).containsExactly(transactionId);
    }

    @Test
    void save_whenTransactionExistsWithDifferentType_mustUpdateTransactionAndChangeTypeMap() {
        Long transactionId = 2L;
        TransactionNode transactionNode = new TransactionNode(transactionId, 20d, null, "newType");
        transactions.put(transactionId, new TransactionNode(transactionId, 40d, null, "type"));
        transactionsTypes.put("type", Sets.newHashSet(transactionId));

        TransactionNode saved = transactionNodeRepository.save(transactionNode);

        assertThat(saved).isNotNull().isEqualTo(transactionNode);
        assertThat(transactions.get(transactionId)).isEqualTo(transactionNode);
        assertThat(transactionsTypes.get("type")).isEmpty();
        assertThat(transactionsTypes.get("newType")).containsExactly(2L);
    }

    @Test
    void save_whenTransactionHasParentAndParentExists_mustAddNewTransaction() {
        TransactionNode transactionNode = new TransactionNode(3L, 20d, 1L, "newType");
        transactions.put(1L, new TransactionNode(1L, 40d, null, "type"));
        transactionsTypes.put("type", Sets.newHashSet(1L));

        TransactionNode saved = transactionNodeRepository.save(transactionNode);

        assertThat(transactions.get(1L).getChilds()).containsExactly(transactionNode);
        assertThat(saved).isNotNull().isEqualTo(transactionNode);
        assertThat(transactions.get(3L)).isEqualTo(transactionNode);
    }

    @Test
    void save_whenExistsTransactionAndHasChilds_updateTransactionAndKeepChilds() {
        TransactionNode transactionNode = new TransactionNode(1L, 40d, null, "type");
        TransactionNode child = new TransactionNode(3L, 40d, 1L, "type");
        transactionNode.addChild(child);
        transactions.put(1L, transactionNode);
        transactions.put(3L, child);

        TransactionNode updateTransactionNode = new TransactionNode(1L, 41d, null, "type");

        transactionNodeRepository.save(updateTransactionNode);

        assertThat(transactions.get(1L).getChilds()).containsExactly(child);
    }

    @Test
    void save_whenExistsTransactionAndChangeParent_updateTransactionAndRemoveFromOriginalChild() {
        TransactionNode transactionNode = new TransactionNode(1L, 40d, null, "type");
        TransactionNode child = new TransactionNode(3L, 40d, 1L, "type");
        transactionNode.addChild(child);
        transactions.put(1L, transactionNode);
        transactions.put(2L, new TransactionNode(2L, 40d, null, "type"));
        transactions.put(3L, child);

        TransactionNode updateTransactionNode = new TransactionNode(3L, 41d, 2L, "type");

        transactionNodeRepository.save(updateTransactionNode);

        assertThat(transactions.get(1L).getChilds()).isEmpty();
        assertThat(transactions.get(2L).getChilds()).containsExactly(updateTransactionNode);
    }

    @Test
    void save_whenExistsTransactionAndSameParent_updateTransactionAndRemoveFromOriginalChild() {
        TransactionNode transactionNode = new TransactionNode(1L, 40d, null, "type");
        TransactionNode child = new TransactionNode(3L, 40d, 1L, "type");
        transactionNode.addChild(child);
        transactions.put(1L, transactionNode);
        transactions.put(2L, new TransactionNode(2L, 40d, null, "type"));
        transactions.put(3L, child);

        TransactionNode updateTransactionNode = new TransactionNode(3L, 41d, 1L, "type");

        transactionNodeRepository.save(updateTransactionNode);

        assertThat(transactions.get(1L).getChilds()).containsExactly(updateTransactionNode);
    }

    @Test
    void save_whenExistsTransactionWithoutParent_updateTransactionAndRemoveFromOriginalChild() {
        TransactionNode transactionNode = new TransactionNode(1L, 40d, null, "type");
        TransactionNode child = new TransactionNode(3L, 40d, null, "type");
        transactions.put(1L, transactionNode);
        transactions.put(2L, new TransactionNode(2L, 40d, null, "type"));
        transactions.put(3L, child);

        TransactionNode updateTransactionNode = new TransactionNode(3L, 41d, 2L, "type");

        transactionNodeRepository.save(updateTransactionNode);

        assertThat(transactions.get(1L).getChilds()).isEmpty();
        assertThat(transactions.get(2L).getChilds()).containsExactly(updateTransactionNode);
    }

    @Test
    void save_whenTransactionHasParentAndParentNotExists_mustThrowException() {
        TransactionNode transactionNode = new TransactionNode(3L, 20d, 1L, "newType");

        assertThatThrownBy(() -> transactionNodeRepository.save(transactionNode))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void getIdByType_whenTypeExistsAndNotEmpty_mustReturnIds() {
        Set<Long> ids = Sets.newHashSet(1L, 2L, 4L, 20L);
        transactionsTypes.put("type", ids);

        Set<Long> responseIds = transactionNodeRepository.getIdByType("type");

        assertThat(responseIds).isEqualTo(ids);
    }

    @Test
    void getIdByType_whenTypeNotExists_mustReturnEmptyList() {
        assertThat(transactionNodeRepository.getIdByType("type")).isEmpty();
    }

    @Test
    void getById_whenTransactionExists_mustReturnTransaction() {
        TransactionNode transactionNode = new TransactionNode(1L, 40d, null, "type");
        transactions.put(1L, transactionNode);

        Optional<TransactionNode> result = transactionNodeRepository.findById(1L);

        assertThat(result).containsSame(transactionNode);
    }

    @Test
    void getById_whenTransactionNotExists_mustReturnEmtpyOptional() {
        assertThat(transactionNodeRepository.findById(1L)).isEmpty();
    }

    @Test
    void exists_whenTransactionExists_mustReturnTrue() {
        TransactionNode transactionNode = new TransactionNode(1L, 40d, null, "type");
        transactions.put(1L, transactionNode);
        assertThat(transactionNodeRepository.exists(1L)).isTrue();
    }

    @Test
    void exists_whenTransactionNotExists_mustReturnFalse() {
        assertThat(transactionNodeRepository.exists(1L)).isFalse();
    }
}
