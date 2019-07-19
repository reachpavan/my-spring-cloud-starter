package com.examples.springcloudstarter.fibonacci.services;

import com.examples.springcloudstarter.fibonacci.ApplicationException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Fibonacci Service interface
 */
@Service
@Scope("prototype")
public interface FibonacciService {

    /**
     * Validate count parameter value of the fibonacci series
     * @param count
     * @return
     * @throws ApplicationException
     */
    Optional<String> validateCount(Integer count) throws ApplicationException;

    /**
     * get fibonacci series for the given count
     * @param count
     * @return
     * @throws ApplicationException
     */
    List<BigInteger> getFibonacciSeries(int count) throws ApplicationException;
}
