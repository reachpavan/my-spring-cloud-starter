package com.examples.springcloudstarter.fibonacci.errors;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessages {
    public static Map<String, String> messages;

    static {
        messages = new HashMap<>();
        messages.put("fibonacci.count.min", "must be greater than or equal to %d");
        messages.put("fibonacci.count.max", "must be less than or equal to %d");
    }

    public static String getMessage(String messageKey, Object... params){
        String message = messages.get(messageKey);
        return message == null ? messageKey : String.format(messageKey, params);
    }
}
