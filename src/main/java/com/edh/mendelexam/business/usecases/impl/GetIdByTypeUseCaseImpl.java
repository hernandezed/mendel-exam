package com.edh.mendelexam.business.usecases.impl;

import com.edh.mendelexam.business.exception.NoSuchTypeException;
import com.edh.mendelexam.business.usecases.GetIdByTypeUseCase;
import com.edh.mendelexam.ports.TransactionNodePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GetIdByTypeUseCaseImpl implements GetIdByTypeUseCase {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private TransactionNodePort transactionNodePort;

    public GetIdByTypeUseCaseImpl(TransactionNodePort transactionNodePort) {
        this.transactionNodePort = transactionNodePort;
    }

    @Override
    public Set<Long> execute(String type) {
        Set<Long> result = transactionNodePort.getIdByType(type);
        if (result.isEmpty()) {
            logger.error("Cannot found ids for type {}", type);
            throw new NoSuchTypeException();
        }
        return result;
    }
}
