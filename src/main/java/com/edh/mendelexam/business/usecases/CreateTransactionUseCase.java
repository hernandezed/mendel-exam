package com.edh.mendelexam.business.usecases;

import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;

public interface CreateTransactionUseCase {
    TransactionNodeBo execute(CreateTransactionBo createTransactionBo);
}
