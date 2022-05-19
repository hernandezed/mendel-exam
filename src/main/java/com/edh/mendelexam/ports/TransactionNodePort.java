package com.edh.mendelexam.ports;

import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;

import java.util.Optional;
import java.util.Set;

public interface TransactionNodePort {

    Optional<TransactionNodeBo> findById(Long id);

    Set<Long> getIdByType(String type);

    TransactionNodeBo save(CreateTransactionBo transactionBo);
}
