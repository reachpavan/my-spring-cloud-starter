package com.examples.springcloudstarter.fibonacci.services;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

@Service
public class FibonacciService {
    public List<BigInteger> getFibonacciSeries(int count) {
        List<BigInteger> fibonacciSeries = new LinkedList<BigInteger>();
        if (count > 0 ) {
            BigInteger a = BigInteger.valueOf(0);
            BigInteger b = BigInteger.valueOf(1);
            BigInteger c = null;
            fibonacciSeries.add(a);
            for (int i = 1; i < count; i++) {
                c = a.add(b);
                a = b;
                b = c;
                fibonacciSeries.add(c);
            }
        }
        return fibonacciSeries;
    }
}
