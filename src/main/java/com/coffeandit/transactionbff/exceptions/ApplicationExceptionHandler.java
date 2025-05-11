package com.coffeandit.transactionbff.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseMessage> unauthorized(UnauthorizedException ex) {
        return responseMessage(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseMessage> transactionNotFoundException(TransactionNotFoundException ex) {
        log.error("Transaction não encontrada.");
        return responseMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponseMessage> responseMessage(String messsage, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(
                        new ErrorResponseMessage(messsage, httpStatus.value())
                );
    }

}
