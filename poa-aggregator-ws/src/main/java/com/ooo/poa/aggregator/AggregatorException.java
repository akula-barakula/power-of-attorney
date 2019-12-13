package com.ooo.poa.aggregator;

public class AggregatorException extends RuntimeException {

    private static final long serialVersionUID = -121035692150541148L;


    public AggregatorException(Exception exception) {
        super(exception);
    }
}
