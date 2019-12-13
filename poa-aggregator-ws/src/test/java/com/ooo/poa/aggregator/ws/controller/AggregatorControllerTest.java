package com.ooo.poa.aggregator.ws.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.ws.model.ModelTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class AggregatorControllerTest extends ModelTest {

	@Configuration
	public static class AggregatorControllerTestConfig {

	    @Bean
	    public AggregatorService aggregatorService() {
	        return Mockito.mock(AggregatorService.class);
	    }

        @Bean
        public AggregatorController poaAggregatorController() {
            return new AggregatorController();
        }
	}


    @Autowired
    private AggregatorController controller;

    @Autowired
    private AggregatorService aggregatorService;


    @Before
    public void before() {
    	standaloneSetup(controller);

    	Mockito.when(aggregatorService.getPowerOfAttorneys(anyString()))
    	        .thenReturn(Arrays.asList(newPoaAggr("id")));
    }


    @Test
	public void getStatus() {

		when().
		        get("/aggregator/status").
		then().
		        statusCode(200).
		        body("message", startsWith("com.ooo.poa.aggregator.ws.controller.AggregatorController is running. Current timestamp is "));
	}

    @Test
	public void getPowerOfAttorneys() {

		when().
		        get("/aggregator/power-of-attorneys?user=name").
		then().
		        statusCode(200).
		        body("[0].id", equalTo("id"));
	}
}
