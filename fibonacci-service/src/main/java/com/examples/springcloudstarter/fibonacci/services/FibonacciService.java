package com.examples.springcloudstarter.fibonacci.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope("prototype")
public class FibonacciService {

    private ConcurrentHashMap<Integer, List<BigInteger>> fibonacciStore = new ConcurrentHashMap<>();

    public List<BigInteger> getFibonacciSeries(int count) {
        Optional<List<BigInteger>> storedSeries = this.findSeriesInStore(count);
        if (storedSeries.isPresent()) {
            return storedSeries.get();
        } else {
            List<BigInteger> series = this.generateFibonacciSeries(count);
            this.addSeriesToStore(count, series);
            return series;
        }
    }

    public List<BigInteger> generateFibonacciSeries(int count) {
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

    public Optional<List<BigInteger>> findSeriesInStore(int count) {
        return Optional.ofNullable(fibonacciStore.get(count));
    }

    public void addSeriesToStore(int count, List<BigInteger> series)  {
        fibonacciStore.put(count, series);
    }

}
