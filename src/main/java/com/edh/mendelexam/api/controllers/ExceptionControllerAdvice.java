package com.edh.mendelexam.api.controllers;

import com.edh.mendelexam.api.dtos.responses.ApiError;
import com.edh.mendelexam.api.dtos.responses.TransactionResponseDto;
import com.edh.mendelexam.business.exception.NoSuchParentException;
import com.edh.mendelexam.business.exception.NoSuchTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NoSuchParentException.class)
    public ResponseEntity<TransactionResponseDto> handleNoSuchParentException(NoSuchParentException ex) {
        return ResponseEntity.badRequest().body(TransactionResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NoSuchTypeException.class)
    public ResponseEntity<?> handleNoSuchTypeException(NoSuchTypeException ex) {
        return ResponseEntity.notFound().build();
    }

}
