package com.ooo.poa.aggregator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.service.impl.DataCollector.CollectedData;
import com.ooo.poa.aggregator.ws.model.PowerOfAttorneyAggr;

public class AggregatorServiceImpl implements AggregatorService {

    @Autowired
    private DataCollector dataCollector;

    @Autowired
    private DataAggregator dataAggregator;


	@Override
	public List<PowerOfAttorneyAggr> getPowerOfAttorneys(String user) {

	    CollectedData data = dataCollector.collectFor(user);

		return dataAggregator.aggregate(
		        data.getPowerOfAttorneys(),
		        data.getAccounts(),
		        data.getCreditCards(),
		        data.getDebitCards());
	}
}
