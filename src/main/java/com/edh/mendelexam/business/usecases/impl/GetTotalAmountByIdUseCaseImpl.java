package com.edh.mendelexam.business.usecases.impl;

import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.usecases.GetTotalAmountByIdUseCase;
import com.edh.mendelexam.ports.TransactionNodePort;

public class GetTotalAmountByIdUseCaseImpl implements GetTotalAmountByIdUseCase {
    private final TransactionNodePort transactionNodePort;

    public GetTotalAmountByIdUseCaseImpl(TransactionNodePort transactionNodePort) {
        this.transactionNodePort = transactionNodePort;
    }

    @Override
    public double execute(Long transactionId) {
        return transactionNodePort.findById(transactionId).map(TransactionNodeBo::getTotal)
                    .orElseThrow();
    }
}
