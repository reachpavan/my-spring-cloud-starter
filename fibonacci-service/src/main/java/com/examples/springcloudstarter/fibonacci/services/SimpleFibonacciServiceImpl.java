package com.examples.springcloudstarter.fibonacci.services;

import com.examples.springcloudstarter.fibonacci.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple Fibonacci Service Implementation using a local store
 */
@Service
@Scope("prototype")
public class SimpleFibonacciServiceImpl implements FibonacciService{
    private final Logger LOGGER = LoggerFactory.getLogger(SimpleFibonacciServiceImpl.class);

    public static final String MSG_PARAMETER_MISSING = "count parameter is missing";
    public static final String MSG_MIN_VALUE = "count must be greater than or equal to 0";
    public static final String MSG_MAX_VALUE = "count must be lesser than or equal to %s";

    @Value("${fibonacci.limit:100000}")
    private int maxLimit;

    private ConcurrentHashMap<Integer, List<BigInteger>> fibonacciStore = new ConcurrentHashMap<>();

    /**
     * Validate count parameter value of the fibonacci series
     * @param count
     * @return
     * @throws ApplicationException
     */
    @Override
    public Optional<String> validateCount(Integer count) throws ApplicationException {
        String msg = null;
        if (count == null) {
            msg = MSG_PARAMETER_MISSING;
        } else if (count < 0) {
            msg = MSG_MIN_VALUE;
        } else if (count > maxLimit) {
            msg = String.format(MSG_MAX_VALUE, maxLimit);
        } else {
            msg = null;
        }

        LOGGER.debug(String.format("Count - %d, Validation Message: %s", count, msg));
        return Optional.ofNullable(msg);
    }

    /**
     * Generate Fibonacci series if it's not in the store.
     * @param count
     * @return
     * @throws ApplicationException
     */
    public List<BigInteger> getFibonacciSeries(int count) throws ApplicationException {
        LOGGER.debug("Get Fibonacci Series for count: " + count);

        if(count < 0 || count > maxLimit) {
            String errorMsg = "Invalid count value: " + count;
            LOGGER.error(errorMsg);
            throw new ApplicationException(errorMsg);
        }

        Optional<List<BigInteger>> storedSeries = this.findSeriesInStore(count);
        if (storedSeries.isPresent()) {
            return storedSeries.get();
        } else {
            List<BigInteger> series = this.generateFibonacciSeries(count);
            this.addSeriesToStore(count, series);
            return series;
        }
    }

    /**
     * Find fibonacci series in store
     * @param count
     * @return
     */
    public Optional<List<BigInteger>> findSeriesInStore(int count) {
        LOGGER.debug("Finding Fibonacci Series in store for count: " + count);

        Optional optinalSeries =  Optional.ofNullable(fibonacciStore.get(count));

        String found = optinalSeries.isPresent() ? "found" : "not found";
        LOGGER.debug(String.format("Fibonacci Series %s in store for count: %d)", found, count));

        return optinalSeries;
    }

    /**
     * Store fibonacci series to store
     * @param count
     * @param series
     */
    public void addSeriesToStore(int count, List<BigInteger> series)  {
        LOGGER.debug("Storing Fibonacci Series in store for count: " + count);
        fibonacciStore.put(count, series);
    }

    /**
     * Generate fibonacci series for the given count
     * @param count
     * @return
     */
    public List<BigInteger> generateFibonacciSeries(int count) {
        LOGGER.debug("Generating Fibonacci Series for count: " + count);

        List<BigInteger> fibonacciSeries = new LinkedList<BigInteger>();
        if (count > 0 ) {
            BigInteger a = BigInteger.valueOf(0);
            BigInteger b = BigInteger.valueOf(1);
            BigInteger c = null;
            fibonacciSeries.add(a);
            if (count > 1) {
                fibonacciSeries.add(b);
            }
            for (int i = 2; i < count; i++) {
                c = a.add(b);
                a = b;
                b = c;
                fibonacciSeries.add(c);
            }
        }

        LOGGER.debug("Generated Fibonacci Series for count: " + count);
        return fibonacciSeries;
    }

}
