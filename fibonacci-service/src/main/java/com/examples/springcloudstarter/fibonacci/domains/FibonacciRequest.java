package com.examples.springcloudstarter.fibonacci.domains;

public class FibonacciRequest {
    @ValidNumber(
            message = "fibonacci.count",
            minConfig = "fibonacci.min",
            maxConfig = "fibonacci.max",
            required = true
    )
    private int count;

    public FibonacciRequest(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
