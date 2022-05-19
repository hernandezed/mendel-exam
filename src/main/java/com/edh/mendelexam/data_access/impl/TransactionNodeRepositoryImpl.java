package com.edh.mendelexam.data_access.impl;

import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;

import java.util.*;

public class TransactionNodeRepositoryImpl implements TransactionNodeRepository {

    private final Map<Long, TransactionNode> transactions;
    private final Map<String, Set<Long>> transactionsCategories;

    public TransactionNodeRepositoryImpl(Map<Long, TransactionNode> transactions, Map<String, Set<Long>> transactionsCategories) {
        this.transactions = transactions;
        this.transactionsCategories = transactionsCategories;
    }

    @Override
    public TransactionNode save(TransactionNode transactionNode) {

        if (transactionNode.getParentId() != null) {
            if (!transactions.containsKey(transactionNode.getParentId())) {
                throw new IllegalStateException("Parent transaction must exists");
            }
            transactions.get(transactionNode.getParentId()).addChild(transactionNode);

        }
        TransactionNode oldTransaction = transactions.get(transactionNode.getId());

        if (oldTransaction != null) {
            if (!transactionNode.getType().equals(oldTransaction.getType())) {
                transactionsCategories.get(oldTransaction.getType()).remove(oldTransaction.getId());
            }
            transactionNode.setChilds(oldTransaction.getChilds());

            if (oldTransaction.getParentId() != null && !Objects.equals(oldTransaction.getParentId(), transactionNode.getParentId())) {
                TransactionNode oldParent = transactions.get(oldTransaction.getParentId());
                oldParent.removeChild(oldTransaction);
            }
        }

        if (!transactionsCategories.containsKey(transactionNode.getType())) {
            transactionsCategories.put(transactionNode.getType(), new HashSet<>());
        }

        transactions.put(transactionNode.getId(), transactionNode);
        transactionsCategories.get(transactionNode.getType()).add(transactionNode.getId());

        return transactionNode;
    }

    @Override
    public Set<Long> getIdByCategory(String type) {
        return Optional.ofNullable(transactionsCategories.get(type)).orElse(new HashSet<>());
    }

    @Override
    public Optional<TransactionNode> findById(Long id) {
        return Optional.ofNullable(transactions.get(id));
    }
}
