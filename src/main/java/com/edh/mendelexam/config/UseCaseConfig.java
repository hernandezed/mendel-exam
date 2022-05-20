package com.edh.mendelexam.config;

import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import com.edh.mendelexam.business.usecases.GetIdByTypeUseCase;
import com.edh.mendelexam.business.usecases.GetTotalAmountByIdUseCase;
import com.edh.mendelexam.business.usecases.impl.CreateTransactionUseCaseImpl;
import com.edh.mendelexam.business.usecases.impl.GetIdByTypeUseCaseImpl;
import com.edh.mendelexam.business.usecases.impl.GetTotalAmountByIdUseCaseImpl;
import com.edh.mendelexam.ports.TransactionNodePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionNodePort transactionNodePort) {
        return new CreateTransactionUseCaseImpl(transactionNodePort);
    }

    @Bean
    public GetTotalAmountByIdUseCase getTotalAmountByIdUseCase(TransactionNodePort transactionNodePort) {
        return new GetTotalAmountByIdUseCaseImpl(transactionNodePort);
    }

    @Bean
    public GetIdByTypeUseCase getIdByTypeUseCase(TransactionNodePort transactionNodePort) {
        return new GetIdByTypeUseCaseImpl(transactionNodePort);
    }
}
