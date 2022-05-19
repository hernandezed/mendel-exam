package com.edh.mendelexam.ports.impl;

import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;
import com.edh.mendelexam.ports.TransactionNodePort;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionNodePortImpl implements TransactionNodePort {
    TransactionNodeRepository transactionNodeRepository;

    public TransactionNodePortImpl(TransactionNodeRepository transactionNodeRepository) {
        this.transactionNodeRepository = transactionNodeRepository;
    }

    @Override
    public Optional<TransactionNodeBo> findById(Long id) {
        return transactionNodeRepository.findById(id)
                .flatMap(transactionNode -> Optional.of(map(transactionNode)));
    }

    @Override
    public Set<Long> getIdByType(String type) {
        return transactionNodeRepository.getIdByType(type);
    }

    @Override
    public TransactionNodeBo save(CreateTransactionBo transactionBo) {
        return map(transactionNodeRepository.save(map(transactionBo)));
    }

    private static TransactionNode map(CreateTransactionBo transactionNodeBo) {
        return new TransactionNode(transactionNodeBo.getId(), transactionNodeBo.getAmount(), transactionNodeBo.getParentId(), transactionNodeBo.getType());
    }

    private static TransactionNodeBo map(TransactionNode transactionNode) {
        return new TransactionNodeBo(transactionNode.getId(), transactionNode.getAmount(), transactionNode.getType(),
                transactionNode.getChilds().stream().map(TransactionNodePortImpl::map).collect(Collectors.toSet()));
    }
}
