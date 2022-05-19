package com.edh.mendelexam.unit.business.usecase;

import com.edh.mendelexam.business.exception.NoSuchTypeException;
import com.edh.mendelexam.business.usecases.GetIdByTypeUseCase;
import com.edh.mendelexam.business.usecases.impl.GetIdByTypeUseCaseImpl;
import com.edh.mendelexam.ports.TransactionNodePort;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class GetIdByTypeUseCaseTest {

    TransactionNodePort transactionNodePort = mock(TransactionNodePort.class);
    GetIdByTypeUseCase getIdByTypeUseCase = new GetIdByTypeUseCaseImpl(transactionNodePort);

    @Test
    void execute_whenExistsTypeWithIds_mustReturnSetOfIds() {
        String type = "type";
        HashSet<Long> ids = Sets.newHashSet(1L, 2L);
        when(transactionNodePort.getIdByType(type)).thenReturn(ids);
        assertThat(getIdByTypeUseCase.execute(type)).containsAll(ids);
        verify(transactionNodePort).getIdByType(type);
    }

    @Test
    void execute_whenNonExistsType_mustThrowException() {
        Logger logger = mock(Logger.class);
        try (MockedStatic<LoggerFactory> loggerMockedStatic = Mockito.mockStatic(LoggerFactory.class)) {
            loggerMockedStatic.when(() -> LoggerFactory.getLogger(GetIdByTypeUseCaseImpl.class)).thenReturn(logger);
            when(transactionNodePort.getIdByType(any())).thenReturn(new HashSet<>());
            GetIdByTypeUseCase getIdByTypeUseCase = new GetIdByTypeUseCaseImpl(transactionNodePort);
            assertThatThrownBy(() -> getIdByTypeUseCase.execute("anyType"))
                    .isInstanceOf(NoSuchTypeException.class);
            verify(transactionNodePort).getIdByType("anyType");
            verify(logger).error("Cannot found ids for type {}", "anyType");
        }
    }
}
