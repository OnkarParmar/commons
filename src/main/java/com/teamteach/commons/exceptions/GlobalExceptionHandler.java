package com.teamteach.commons.exceptions;

import com.teamteach.commons.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        List<String> fieldErrors = ex.getBindingResult().getAllErrors().stream()
                .map( a -> ((FieldError)a).getField() + " " + ((FieldError)a).getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(ApiResponse.createFailedResponse(status.value(),fieldErrors));
    }

    @ExceptionHandler(value
            = { Exception.class})
    public final ResponseEntity<ApiResponse> handleAllExceptions(Exception ex, WebRequest request) {
        String message = ex.getMessage();
        ApiResponse response = ApiResponse.createFailedResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
