package com.examples.springcloudstarter.fibonacci.errors;

import com.examples.springcloudstarter.fibonacci.errors.ErrorResponse.ErrorType;
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
import java.util.Arrays;
import java.util.List;

//@ControllerAdvice
//@RestController
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
        // return
        return buildErrorResponse(ErrorType.VALIDATION_ERROR, errors, request);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Error error = new Error(exception.getMessage(), exception.getParameterName());
        // return
        return buildErrorResponse(ErrorType.MISSING_PARAM, error, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchExceptionException(
            MethodArgumentTypeMismatchException exception,
            WebRequest request) {
        // return
        return buildErrorResponse(ErrorType.MISSING_PARAM, new Error(exception.getMessage()), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(
            Exception exception,
            WebRequest request) {
        // return
        return buildErrorResponse(ErrorType.SERVER_ERROR, new Error(exception.getMessage()), request);
    }

    protected ResponseEntity<Object> buildErrorResponse(
            ErrorType errorType,
            Error error,
            WebRequest request) {
        // return
        return buildErrorResponse(errorType, Arrays.asList(error), request);
    }

    protected ResponseEntity<Object> buildErrorResponse(
            ErrorType errorType,
            List<Error> errors,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withErrorType(errorType)
                .withErrors(errors)
                .build();
        errorResponse.add(buildLinkFromRequest(request));
        // return
        return new ResponseEntity<>(errorResponse, getHttpStatus(errorType));
    }

    protected Link buildLinkFromRequest(WebRequest request) {

        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        StringBuffer requestURL = servletRequest.getRequestURL();
        if (servletRequest.getQueryString() != null && !servletRequest.getQueryString().isEmpty()) {
            requestURL.append("?").append(servletRequest.getQueryString());
        }
        // return
        return new Link(requestURL.toString());
    }

    protected HttpStatus getHttpStatus(ErrorType errorType) {

        switch (errorType) {
            case VALIDATION_ERROR:
            case MISSING_PARAM:
                return HttpStatus.BAD_REQUEST;
            case SERVER_ERROR:
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}