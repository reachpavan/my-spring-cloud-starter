package com.examples.springcloudstarter.fibonacci.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.examples.springcloudstarter.fibonacci.domains.FibonacciSeries;
import com.examples.springcloudstarter.fibonacci.services.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@Validated
public class FibonacciController {

    private static final String TEMPLATE = "Hello, %s!";

    @Autowired
    private FibonacciService fibonacciService;

    @RequestMapping("/fibonacci")
    public HttpEntity<FibonacciSeries> getFibonacciSeries(
            @RequestParam(value = "count", required = true)
            @Min(value = 1, message = "fibonacci.count.min")
            @Max(value = 100000, message = "fibonacci.count.max") int count) {

        FibonacciSeries fibonacciSeries = new FibonacciSeries(fibonacciService.getFibonacciSeries(count));
        fibonacciSeries.add(linkTo(methodOn(FibonacciController.class).getFibonacciSeries(count)).withSelfRel());

        return new ResponseEntity<>(fibonacciSeries, HttpStatus.OK);
    }
}