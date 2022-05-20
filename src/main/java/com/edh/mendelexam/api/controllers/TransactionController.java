package com.edh.mendelexam.api.controllers;

import com.edh.mendelexam.api.dtos.requests.CreateTransactionDto;
import com.edh.mendelexam.api.dtos.responses.SumResponseDto;
import com.edh.mendelexam.api.dtos.responses.TransactionResponseDto;
import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import com.edh.mendelexam.business.usecases.GetTotalAmountByIdUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTotalAmountByIdUseCase getTotalAmountByIdUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, GetTotalAmountByIdUseCase getTotalAmountByIdUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTotalAmountByIdUseCase = getTotalAmountByIdUseCase;
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> createOrUpdate(@PathVariable Long transactionId, @Valid @RequestBody CreateTransactionDto createTransactionDto) {
        TransactionNodeBo transactionNodeBo = createTransactionUseCase.execute(map(transactionId, createTransactionDto));
        if (transactionNodeBo.isUpdate()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(map(transactionNodeBo), HttpStatus.CREATED);
    }

    @GetMapping("/sum/{transactionId}")
    public ResponseEntity<SumResponseDto> getSum(@PathVariable Long transactionId) {
        return ResponseEntity.ok(new SumResponseDto(getTotalAmountByIdUseCase.execute(transactionId)));
    }

    private CreateTransactionBo map(Long id, CreateTransactionDto createTransactionDto) {
        return new CreateTransactionBo(id, createTransactionDto.getAmount(), createTransactionDto.getParentId(), createTransactionDto.getType());
    }

    private TransactionResponseDto map(TransactionNodeBo transactionNodeBo) {
        return new TransactionResponseDto(transactionNodeBo.getId(), transactionNodeBo.getType(), transactionNodeBo.getAmount());
    }
}
