package com.edh.mendelexam.data_access;

import java.util.Optional;
import java.util.Set;

public interface TransactionNodeRepository {
    TransactionNode save(TransactionNode transactionNode);

    Set<Long> getIdByType(String type);

    Optional<TransactionNode> findById(Long id);
}
