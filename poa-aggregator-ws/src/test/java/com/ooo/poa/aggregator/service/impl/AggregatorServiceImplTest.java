package com.ooo.poa.aggregator.service.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.service.PoaClient;
import com.ooo.poa.aggregator.service.impl.DataCollector.CollectedData;
import com.ooo.poa.aggregator.ws.model.PowerOfAttorneyAggr;

@RunWith(SpringJUnit4ClassRunner.class)
public class AggregatorServiceImplTest {

	@Configuration
	public static class AggregatorServiceImplTestConfig {

        @Bean
        public PoaClient poaClient() {
            return mock(PoaClient.class);
        }

	    @Bean
	    public DataCollector dataCollector() {
	        return mock(DataCollector.class);
	    }

        @Bean
        public DataAggregator dataAggregator() {
            return mock(DataAggregator.class);
        }

	    @Bean
	    public AggregatorService aggregatorService() {
	        return new AggregatorServiceImpl();
	    }
	}


    @Autowired
    private AggregatorService service;

    @Autowired
    private DataCollector dataCollector;

    @Autowired
    private DataAggregator dataAggregator;


    @Test
	@SuppressWarnings("unchecked")
	public void getPowerOfAttorneys() {
	    CollectedData data = new CollectedData();
	    List<PowerOfAttorneyAggr> aggregated = mock(List.class);

	    when(dataCollector.collectFor(anyString()))
	            .thenReturn(data);
	    when(dataAggregator.aggregate(data.getPowerOfAttorneys(), data.getAccounts(), data.getCreditCards(), data.getDebitCards()))
	            .thenReturn(aggregated);


	    assertSame(aggregated, service.getPowerOfAttorneys("string"));
	}
}
