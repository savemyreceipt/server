package com.savemyreceipt.smr.controller;

import com.savemyreceipt.smr.DTO.ApiResponseDto;
import com.savemyreceipt.smr.exception.model.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(ApiResponseDto.error(e.getErrorStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errMessage = new StringBuilder();

        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                .append(error.getField())
                .append("] ")
                .append(":")
                .append(error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseDto.error(e, errMessage.toString()));
    }
}
