package com.examples.springcloudstarter.fibonacci.dto;

import org.springframework.http.HttpStatus;

/**
 * create by lakshmiarepu on 2019-07-18
 */
public class ErrorResponse {
    HttpStatus status;
    String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
