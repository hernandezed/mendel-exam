package com.edh.mendelexam.api.controllers;

import com.edh.mendelexam.api.dtos.requests.CreateTransactionDto;
import com.edh.mendelexam.api.dtos.responses.TransactionResponseDto;
import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> createOrUpdate(@PathVariable Long transactionId, @RequestBody CreateTransactionDto createTransactionDto) {
        TransactionNodeBo transactionNodeBo = createTransactionUseCase.execute(map(transactionId, createTransactionDto));
        return new ResponseEntity<>(map(transactionNodeBo), HttpStatus.CREATED);
    }

    private CreateTransactionBo map(Long id, CreateTransactionDto createTransactionDto) {
        return new CreateTransactionBo(id, createTransactionDto.getAmount(), null, createTransactionDto.getType());
    }

    private TransactionResponseDto map(TransactionNodeBo transactionNodeBo) {
        return new TransactionResponseDto(transactionNodeBo.getId(), transactionNodeBo.getType(), transactionNodeBo.getAmount());
    }
}
