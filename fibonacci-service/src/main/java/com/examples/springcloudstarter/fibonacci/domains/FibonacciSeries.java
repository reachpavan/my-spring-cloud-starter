package com.examples.springcloudstarter.fibonacci.domains;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public class FibonacciSeries extends ResourceSupport {

    private final List<BigInteger> series;

    @JsonCreator
    public FibonacciSeries(@JsonProperty("series") List<BigInteger> series) {
        this.series = series;
    }

    public List<BigInteger> getSeries() {
        return series;
    }
}