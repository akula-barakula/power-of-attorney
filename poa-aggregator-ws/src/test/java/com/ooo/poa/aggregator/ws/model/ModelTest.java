package com.ooo.poa.aggregator.ws.model;

public abstract class ModelTest {

    public PowerOfAttorneyAggr newPoa(String id) {

        PowerOfAttorneyAggr result = new PowerOfAttorneyAggr();

        result.setId(id);

        return result;
    }
}
