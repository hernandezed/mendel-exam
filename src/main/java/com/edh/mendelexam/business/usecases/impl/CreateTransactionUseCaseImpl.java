package com.edh.mendelexam.business.usecases.impl;

import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.exception.NoSuchParentException;
import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import com.edh.mendelexam.ports.TransactionNodePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private TransactionNodePort transactionNodePort;

    public CreateTransactionUseCaseImpl(TransactionNodePort transactionNodePort) {
        this.transactionNodePort = transactionNodePort;
    }

    @Override
    public TransactionNodeBo execute(CreateTransactionBo createTransactionBo) {
        try {
            return transactionNodePort.save(createTransactionBo);
        } catch (NoSuchParentException ex) {
            logger.error("Error creating transaxtion with id=[{}]", createTransactionBo.getId(), ex);
            throw ex;
        }
    }
}
