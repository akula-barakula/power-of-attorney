package com.ooo.poa.aggregator.service.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

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
import com.ooo.poa.client.model.Account;
import com.ooo.poa.client.model.CreditCard;
import com.ooo.poa.client.model.DebitCard;
import com.ooo.poa.client.model.PowerOfAttorney;

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
	    List<PowerOfAttorney> powerOfAttorneys = mock(List.class);
	    Map<String, Account> accounts = mock(Map.class);
	    Map<String, CreditCard> creditCards = mock(Map.class);
	    Map<String, DebitCard> debitCards = mock(Map.class);
	    CollectedData data = new CollectedData(powerOfAttorneys, accounts, creditCards, debitCards);
	    List<PowerOfAttorneyAggr> aggregated = mock(List.class);

	    when(dataCollector.collect())
	            .thenReturn(data);
	    when(dataAggregator.aggregate(powerOfAttorneys, accounts, creditCards, debitCards))
	            .thenReturn(aggregated);


	    assertSame(aggregated, service.getPowerOfAttorneys());
	}
}
