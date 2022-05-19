package com.edh.mendelexam.data_access.impl;

import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;

import java.util.*;

public class TransactionNodeRepositoryImpl implements TransactionNodeRepository {

    private final Map<Long, TransactionNode> transactions;
    private final Map<String, Set<Long>> transactionsTypes;

    public TransactionNodeRepositoryImpl(Map<Long, TransactionNode> transactions, Map<String, Set<Long>> transactionsTypes) {
        this.transactions = transactions;
        this.transactionsTypes = transactionsTypes;
    }

    @Override
    public TransactionNode save(TransactionNode transactionNode) {
        TransactionNode oldTransaction = transactions.get(transactionNode.getId());
        manageRelations(transactionNode, oldTransaction);
        manageCategory(transactionNode, oldTransaction);
        transactions.put(transactionNode.getId(), transactionNode);
        return transactionNode;
    }

    private void manageCategory(TransactionNode transactionNode, TransactionNode oldTransaction) {
        if (oldTransaction != null) {
            Optional.ofNullable(transactionsTypes.get(oldTransaction.getType()))
                    .ifPresent(type -> type.removeIf((id) -> id.equals(oldTransaction.getId())));
            transactionNode.setChilds(oldTransaction.getChilds());
        }
        transactionsTypes.putIfAbsent(transactionNode.getType(), new HashSet<>());
        transactionsTypes.get(transactionNode.getType()).add(transactionNode.getId());
    }

    private void manageRelations(TransactionNode transactionNode, TransactionNode oldTransaction) {
        if (transactionNode.getParentId() != null) {
            TransactionNode parent = Optional.ofNullable(transactions.get(transactionNode.getParentId()))
                    .orElseThrow(() -> new IllegalStateException("Parent transaction must exists"));
            parent.addChild(transactionNode);

            if (!Objects.isNull(oldTransaction) && !Objects.isNull(oldTransaction.getParentId()) && !Objects.equals(oldTransaction.getParentId(), transactionNode.getParentId())) {
                TransactionNode oldParent = transactions.get(oldTransaction.getParentId());
                oldParent.removeChild(oldTransaction);
            }
        }
    }

    @Override
    public Set<Long> getIdByType(String type) {
        return Optional.ofNullable(transactionsTypes.get(type)).orElse(new HashSet<>());
    }

    @Override
    public Optional<TransactionNode> findById(Long id) {
        return Optional.ofNullable(transactions.get(id));
    }
}
