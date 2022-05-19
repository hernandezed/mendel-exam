package com.edh.mendelexam.unit.ports.impl;

import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.data_access.TransactionNode;
import com.edh.mendelexam.data_access.TransactionNodeRepository;
import com.edh.mendelexam.ports.TransactionNodePort;

import javax.swing.text.html.Option;
import java.util.Optional;
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

    private static TransactionNodeBo map(TransactionNode transactionNode) {
        return new TransactionNodeBo(transactionNode.getId(), transactionNode.getAmount(), transactionNode.getType(),
                transactionNode.getChilds().stream().map(TransactionNodePortImpl::map).collect(Collectors.toSet()));
    }
}
