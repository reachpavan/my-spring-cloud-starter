package com.examples.springcloudstarter.fibonacci.errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse extends ResourceSupport {

    public enum ErrorType {
        VALIDATION_ERROR, MISSING_PARAM, SERVER_ERROR;
    }

    private final ErrorType errorType;
    private final List<Error> errors;

    @JsonCreator
    public ErrorResponse(@JsonProperty("error") ErrorType errorType,
                         @JsonProperty("details") List<Error> errors) {
        this.errorType = errorType;
        this.errors = errors;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public static class Builder {
        private ErrorType errorType;
        private List<Error> errors = new ArrayList<>();

        public ErrorResponse build() {
            return new ErrorResponse(errorType, errors);
        }

        public Builder withErrorType(ErrorType errorType) {
            this.errorType = errorType;
            return this;
        }

        public Builder withError(Error error) {
            this.errors.add(error);
            return this;
        }

        public Builder withErrors(List<Error> errors) {
            this.errors.addAll(errors);
            return this;
        }
    }

}