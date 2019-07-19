package com.examples.springcloudstarter.fibonacci.dto;

import java.math.BigInteger;
import java.util.List;

/**
 * create by lakshmiarepu on 2019-07-18
 */
public class FibonacciResponse {
    List<BigInteger> series;

    public FibonacciResponse(List<BigInteger> series) {
        this.series = series;
    }

    public List<BigInteger> getSeries() {
        return series;
    }
}
