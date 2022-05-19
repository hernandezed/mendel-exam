package com.edh.mendelexam.unit.business.usecase;

import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.exception.NoSuchParentException;
import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import com.edh.mendelexam.business.usecases.impl.CreateTransactionUseCaseImpl;
import com.edh.mendelexam.ports.TransactionNodePort;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateTransactionUseCaseTest {

    TransactionNodePort transactionNodePort = mock(TransactionNodePort.class);
    CreateTransactionUseCase createTransactionUseCase = new CreateTransactionUseCaseImpl(transactionNodePort);

    @Test
    void execute_withValidTransaction_mustReturnTransactionCreated() {
        CreateTransactionBo createTransactionBo = new CreateTransactionBo(1L, 20d, null, "type");
        TransactionNodeBo transactionNodeBo = new TransactionNodeBo(1L, 20d, "type", Sets.newHashSet());
        when(transactionNodePort.save(createTransactionBo)).thenReturn(transactionNodeBo);

        TransactionNodeBo result = createTransactionUseCase.execute(createTransactionBo);

        assertThat(result).usingRecursiveComparison().isEqualTo(transactionNodeBo);
        verify(transactionNodePort).save(createTransactionBo);
    }

    @Test
    void execute_withInvalidParentId_mustThrowException() {
        Logger logger = mock(Logger.class);
        try (MockedStatic<LoggerFactory> loggerMockedStatic = Mockito.mockStatic(LoggerFactory.class)) {
            CreateTransactionBo createTransactionBo = new CreateTransactionBo(1L, 20d, 50L, "type");
            loggerMockedStatic.when(() -> LoggerFactory.getLogger(CreateTransactionUseCaseImpl.class)).thenReturn(logger);
            CreateTransactionUseCase createTransactionUseCase = new CreateTransactionUseCaseImpl(transactionNodePort);
            when(transactionNodePort.save(any())).thenThrow(new NoSuchParentException(new IllegalStateException()));
            assertThatThrownBy(() -> createTransactionUseCase.execute(createTransactionBo)).isInstanceOf(NoSuchParentException.class);
            verify(logger).error(eq("Error creating transaxtion with id=[{}]"), eq(1L), any(NoSuchParentException.class));
        }
    }
}
