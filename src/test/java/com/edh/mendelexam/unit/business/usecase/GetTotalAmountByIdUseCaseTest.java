package com.edh.mendelexam.unit.business.usecase;

import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.usecases.GetTotalAmountByIdUseCase;
import com.edh.mendelexam.business.usecases.impl.GetTotalAmountByIdUseCaseImpl;
import com.edh.mendelexam.ports.TransactionNodePort;
import com.edh.mendelexam.ports.impl.TransactionNodePortImpl;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class GetTotalAmountByIdUseCaseTest {

    TransactionNodePort transactionNodePort = mock(TransactionNodePort.class);
    GetTotalAmountByIdUseCase getTotalAmountByIdUseCase = new GetTotalAmountByIdUseCaseImpl(transactionNodePort);

    @Test
    void execute_whenTransactionExists_mustReturnTotalAmount() {
        TransactionNodeBo transactionNodeBo = mock(TransactionNodeBo.class);
        when(transactionNodePort.findById(1L)).thenReturn(Optional.of(transactionNodeBo));
        when(transactionNodeBo.getTotal()).thenReturn(1000d);

        double result = getTotalAmountByIdUseCase.execute(1L);

        assertThat(result).isEqualTo(1000d);
        verify(transactionNodeBo).getTotal();
        verify(transactionNodePort).findById(1L);
    }

    @Test
    void execute_whenTransactionNotExists_mustThrowException() {
        when(transactionNodePort.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> getTotalAmountByIdUseCase.execute(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
