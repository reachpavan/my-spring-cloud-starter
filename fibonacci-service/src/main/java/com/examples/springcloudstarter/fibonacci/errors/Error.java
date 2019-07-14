package com.examples.springcloudstarter.fibonacci.errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private String message;
    private String messageKey;
    private String field;

    @JsonCreator
    public Error(@JsonProperty("message") String message) {
        this(message, null, null);
    }

    @JsonCreator
    public Error(@JsonProperty("message") String message, @JsonProperty("messageKey") String messageKey) {
        this(message, messageKey, null);
    }

    @JsonCreator
    public Error(@JsonProperty("message") String message, @JsonProperty("messageKey") String messageKey, @JsonProperty("field") String field) {
        this.message = message;
        this.messageKey = messageKey;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getField() {
        return field;
    }
}