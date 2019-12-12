package com.ooo.poa.aggregator.service.impl;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.service.PoaClient;

@RunWith(SpringJUnit4ClassRunner.class)
public class AggregatorServiceImplTest {

	@Configuration
	public static class GameControllerTestConfig {

	    @Bean
	    public PoaClient poaClient() {
	        return mock(PoaClient.class);
	    }

	    @Bean
	    public AggregatorService aggregatorService() {
	        return new AggregatorServiceImpl();
	    }
	}


    @Autowired
    private AggregatorService service;

    @Autowired
    private PoaClient poaClient;


	@Before
	public void before() {
//		poaClient.
	}


	@Test
	public void getPowerOfAttorneys() {

	}
}
