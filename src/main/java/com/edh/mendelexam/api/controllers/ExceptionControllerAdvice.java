package com.edh.mendelexam.api.controllers;

import com.edh.mendelexam.api.dtos.responses.ApiError;
import com.edh.mendelexam.api.dtos.responses.TransactionResponseDto;
import com.edh.mendelexam.business.exception.NoSuchParentException;
import com.edh.mendelexam.business.exception.NoSuchTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NoSuchParentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public TransactionResponseDto handleNoSuchParentException(NoSuchParentException ex) {
        return TransactionResponseDto.error(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoSuchElementException(NoSuchElementException ex) {
    }

    @ExceptionHandler(NoSuchTypeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoSuchTypeException(NoSuchTypeException ex) {

    }

}
