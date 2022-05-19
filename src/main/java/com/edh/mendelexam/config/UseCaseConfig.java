package com.edh.mendelexam.config;

import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import com.edh.mendelexam.business.usecases.impl.CreateTransactionUseCaseImpl;
import com.edh.mendelexam.ports.TransactionNodePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionNodePort transactionNodePort) {
        return new CreateTransactionUseCaseImpl(transactionNodePort);
    }
}
