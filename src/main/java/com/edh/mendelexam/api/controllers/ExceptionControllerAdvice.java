package com.edh.mendelexam.api.controllers;

import com.edh.mendelexam.api.dtos.responses.ApiError;
import com.edh.mendelexam.business.exception.NoSuchParentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NoSuchParentException.class)
    public ResponseEntity<ApiError> handleNoSuchParentException(NoSuchParentException ex) {
        return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
    }
}