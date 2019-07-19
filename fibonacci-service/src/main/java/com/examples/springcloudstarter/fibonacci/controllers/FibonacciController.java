package com.examples.springcloudstarter.fibonacci.controllers;

import com.examples.springcloudstarter.fibonacci.ApplicationException;
import com.examples.springcloudstarter.fibonacci.dto.ErrorResponse;
import com.examples.springcloudstarter.fibonacci.dto.FibonacciResponse;
import com.examples.springcloudstarter.fibonacci.services.FibonacciService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@Validated
public class FibonacciController {
    private final Logger LOGGER = LoggerFactory.getLogger(FibonacciController.class);

    @Autowired
    private FibonacciService fibonacciService;

    /**
     * Fibonacci Service resource
     * @param count
     * @return
     */
    @RequestMapping("/fibonacci")
    public ResponseEntity<Object> getFibonacciSeries(
            @RequestParam(name = "count", required = false) Integer count) {

        LOGGER.debug("Request received with count: " + count);

        ResponseEntity<Object> responseEntity = null;
        try {
            Optional<String> optionalValidationError = fibonacciService.validateCount(count);
            if(optionalValidationError.isPresent()) {

                LOGGER.info(String.format("Count: %d - Validation Error: %s", count, optionalValidationError.get()));
                responseEntity = new ResponseEntity(
                        new ErrorResponse(BAD_REQUEST, optionalValidationError.get()),
                        BAD_REQUEST);
            } else {

                responseEntity = new ResponseEntity(
                        new FibonacciResponse(fibonacciService.getFibonacciSeries(count)),
                        OK);
            }
        } catch (ApplicationException ex) {

            LOGGER.error(String.format("Exception occurred for count: %d", count), ex);
            responseEntity = new ResponseEntity<>(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }

        LOGGER.debug("Sending response for count: " + count);
        return responseEntity;
    }

}