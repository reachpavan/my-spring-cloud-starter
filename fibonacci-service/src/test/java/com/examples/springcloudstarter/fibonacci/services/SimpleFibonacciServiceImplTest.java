package com.examples.springcloudstarter.fibonacci.services;

import com.examples.springcloudstarter.fibonacci.ApplicationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigInteger.valueOf;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleFibonacciServiceImplTest {

    @Autowired
    SimpleFibonacciServiceImpl service;

    @Test
    public void validateCount() throws ApplicationException {
        assertTrue("when empty create error", service.validateCount(null).isPresent());
        assertTrue("when min create error", service.validateCount(-1).isPresent());
        assertTrue("when max create error", service.validateCount(100001).isPresent());
        assertFalse("when good value shouldn't create error", service.validateCount(10).isPresent());
    }

    @Test
    public void getFibonacciSeries() throws ApplicationException {
        ApplicationException exception = null;
        try {
            service.getFibonacciSeries(-1);
        } catch (ApplicationException ex) {
            exception = ex;
        }

        assertNotNull("throw exception for negative cases", exception);

        assertEquals("count should match series length", 5 , service.getFibonacciSeries(5).size());

        assertArrayEquals("series should start with zero",
                bigIntegerArray(0, 1, 1, 2, 3), service.getFibonacciSeries(5).toArray());
    }

    @Test
    public void findSeriesInStore() {

        assertFalse("key is not in store", service.findSeriesInStore(5).isPresent());

        BigInteger[] expected = bigIntegerArray(0, 1, 1, 2, 3);
        service.addSeriesToStore(5, Arrays.asList(expected));
        assertArrayEquals("match store values",
                expected, service.findSeriesInStore(5).get().toArray());
    }

    @Test
    public void addSeriesToStore() {
        BigInteger[] expected = bigIntegerArray(0, 1, 1, 2, 3);
        service.addSeriesToStore(5, Arrays.asList(expected));
        assertArrayEquals("match store values",
                expected, service.findSeriesInStore(5).get().toArray());
    }

    @Test
    public void generateFibonacciSeries() {
        assertArrayEquals("test with -1",
                service.generateFibonacciSeries(-1).toArray(), bigIntegerArray());
        assertArrayEquals("test with 0",
                service.generateFibonacciSeries(0).toArray(), bigIntegerArray());
        assertArrayEquals("test with 1",
                service.generateFibonacciSeries(1).toArray(), bigIntegerArray(0));
        assertArrayEquals("test with 2",
                service.generateFibonacciSeries(2).toArray(), bigIntegerArray(0, 1));
        assertArrayEquals("test with 3",
                service.generateFibonacciSeries(3).toArray(), bigIntegerArray(0, 1, 1));
        assertArrayEquals("test with 4",
                service.generateFibonacciSeries(4).toArray(), bigIntegerArray(0, 1, 1, 2));
        assertArrayEquals("test with 5",
                service.generateFibonacciSeries(5).toArray(), bigIntegerArray(0, 1, 1, 2, 3));

        BigInteger actual = new BigInteger("222232244629420445529739893461909967206666939096499764990979600");
        assertEquals("test with higher integer value",
                service.generateFibonacciSeries(301).get(300), actual);
    }

    private BigInteger[] bigIntegerArray(long ... args) {
        return LongStream.of(args).boxed().map(BigInteger::valueOf).toArray(BigInteger[]::new);
    }
}