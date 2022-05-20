package com.edh.mendelexam.api.controllers;

import com.edh.mendelexam.api.dtos.requests.CreateTransactionDto;
import com.edh.mendelexam.api.dtos.responses.SumResponseDto;
import com.edh.mendelexam.api.dtos.responses.TransactionResponseDto;
import com.edh.mendelexam.business.bos.CreateTransactionBo;
import com.edh.mendelexam.business.bos.TransactionNodeBo;
import com.edh.mendelexam.business.usecases.CreateTransactionUseCase;
import com.edh.mendelexam.business.usecases.GetIdByTypeUseCase;
import com.edh.mendelexam.business.usecases.GetTotalAmountByIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTotalAmountByIdUseCase getTotalAmountByIdUseCase;
    private final GetIdByTypeUseCase getIdByTypeUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, GetTotalAmountByIdUseCase getTotalAmountByIdUseCase, GetIdByTypeUseCase getIdByTypeUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTotalAmountByIdUseCase = getTotalAmountByIdUseCase;
        this.getIdByTypeUseCase = getIdByTypeUseCase;
    }

    @Operation(description = "Upsert Transaction (if transaction exists, uodate, else insert new transaction).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "New transaction was created", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class))),
            @ApiResponse(responseCode = "200", description = "Transaction was updated", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "Invalid parent id or amount", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class)))
    })
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> createOrUpdate(@Parameter(description = "The transaction id to upsert") @PathVariable Long transactionId,
                                                                 @Valid @RequestBody CreateTransactionDto createTransactionDto) {
        TransactionNodeBo transactionNodeBo = createTransactionUseCase.execute(map(transactionId, createTransactionDto));
        if (transactionNodeBo.isUpdate()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(TransactionResponseDto.ok(), HttpStatus.CREATED);
    }

    @Operation(description = "Return recursive sum of amounts of transaction with transaction_id and its transaction childs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction exists. Its return sum", content = @Content(schema = @Schema(implementation = SumResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not exists", content = @Content(schema = @Schema()))
    })
    @GetMapping("/sum/{transactionId}")
    public ResponseEntity<SumResponseDto> getSum(@PathVariable Long transactionId) {
        return ResponseEntity.ok(new SumResponseDto(getTotalAmountByIdUseCase.execute(transactionId)));
    }

    @Operation(description = "Return transaction ids grouped by type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Type exists. Return set of transaction ids", content = @Content(schema = @Schema(implementation = Set.class))),
            @ApiResponse(responseCode = "404", description = "Type not exists", content = @Content(schema = @Schema()))
    })
    @GetMapping("/types/{type}")
    public ResponseEntity<Set<Long>> getIdsByType(@PathVariable String type) {
        return ResponseEntity.ok(getIdByTypeUseCase.execute(type));
    }

    private CreateTransactionBo map(Long id, CreateTransactionDto createTransactionDto) {
        return new CreateTransactionBo(id, createTransactionDto.getAmount(), createTransactionDto.getParentId(), createTransactionDto.getType());
    }
}
