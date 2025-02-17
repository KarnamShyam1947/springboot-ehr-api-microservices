package com.ehr.controllers;

import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ehr.dto.ApiErrorResponse;
import com.ehr.exceptions.EntityAlreadyExistsException;

import feign.FeignException.BadRequest;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final HttpServletRequest request;

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatusCode(HttpStatus.CONFLICT.value());

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }
 
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }
    
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage("Invalid user credentials provided");
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }
    
    @ExceptionHandler(value = BadRequest.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequest e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage("Bad request : submit form without error");
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        BindingResult bindingResult = ((MethodArgumentNotValidException)e).getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        Map<String, Object> errors = allErrors.stream()
        .collect(Collectors.toMap(
            error -> ((FieldError)error).getField(),
            ObjectError::getDefaultMessage
        ));
        errorResponse.setErrors(errors);

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage("JWT Token is expired. Please renew to continue");
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(value = SignatureException.class)
    public ResponseEntity<ApiErrorResponse> handleSignatureException(SignatureException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();

        errorResponse.setPath(request.getServletPath());
        errorResponse.setMessage("JWT signature does not match locally computed signature.");
        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

 
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRunTimeException(RuntimeException e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();      

        errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setPath(request.getServletPath());
    
        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();      

        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setPath(request.getServletPath());
    
        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

}