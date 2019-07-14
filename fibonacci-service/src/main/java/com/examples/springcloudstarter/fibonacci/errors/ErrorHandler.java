package com.examples.springcloudstarter.fibonacci.errors;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<Error> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String messageKey = violation.getMessage();
            String message = ErrorMessages.getMessage(messageKey, violation.getInvalidValue());
            String field = violation.getPropertyPath().toString();
            errors.add(new Error(message, messageKey, field));
        }

        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ErrorType.VALIDATION_ERROR, errors);
        errorResponse.add(getLink(request).withSelfRel());

        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(exception.getMessage(), exception.getParameterName()));

        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ErrorType.MISSING_PARAM, errors);
        errorResponse.add(getLink(request).withSelfRel());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchExceptionException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(exception.getMessage()));

        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ErrorType.MISSING_PARAM, errors);
        errorResponse.add(getLink(request).withSelfRel());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        List<Error> errors = new ArrayList<>();
        errors.add(new Error(exception.getMessage()));

        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ErrorType.SERVER_ERROR, errors);
        errorResponse.add(getLink(request).withSelfRel());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Link getLink(WebRequest request) {
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        StringBuffer requestURL = servletRequest.getRequestURL();
        if (servletRequest.getQueryString() != null && !servletRequest.getQueryString().isEmpty()) {
            requestURL.append("?").append(servletRequest.getQueryString());
        }
        return new Link(requestURL.toString());
    }
}