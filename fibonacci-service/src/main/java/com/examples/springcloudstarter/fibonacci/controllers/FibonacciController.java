package com.examples.springcloudstarter.fibonacci.controllers;

import com.examples.springcloudstarter.fibonacci.domains.FibonacciRequest;
import com.examples.springcloudstarter.fibonacci.domains.FibonacciSeries;
import com.examples.springcloudstarter.fibonacci.services.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Validated
public class FibonacciController {

    @Value("${fibonacci.limit:100000}")
    private int maxValue;

    @Autowired
    private FibonacciService fibonacciService;

    @RequestMapping("/fibonacci")
    public HttpEntity<FibonacciSeries> getFibonacciSeries(
            @Valid FibonacciRequest param) {

        FibonacciSeries fibonacciSeries = new FibonacciSeries(fibonacciService.getFibonacciSeries(param.getCount()));
        fibonacciSeries.add(
                linkTo(
                        methodOn(FibonacciController.class).getFibonacciSeries(param)
                ).withSelfRel()
        );

        return new ResponseEntity<>(fibonacciSeries, HttpStatus.OK);
    }
}