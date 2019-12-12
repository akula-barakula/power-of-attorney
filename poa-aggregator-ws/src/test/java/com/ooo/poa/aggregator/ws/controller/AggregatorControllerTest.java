package com.ooo.poa.aggregator.ws.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.restassured.http.ContentType;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
public class AggregatorControllerTest {

	@Configuration
	public static class GameControllerTestConfig {



	    @Bean
	    public ResponseBuilder responseBuilder() {
	        return new ResponseBuilder();
	    }

	    @Bean
	    public AggregatorController poaAggregatorController() {
	        return new AggregatorController();
	    }
	}


    @Autowired
    private AggregatorController controller;


    @Before
    public void before() {
    	standaloneSetup(controller);
    }


    @Test
	public void ping() {

		when().
		        get("/ping").
		then().
		        statusCode(200).
		        body("message", startsWith("ping for com.ooo.kalah.ws.controller.GameController is OK on"));
	}

    @Test
	public void games() {

    	given().
                contentType(ContentType.JSON).
		when().
		        post("/games").
		then().
		        statusCode(201).
		        body("id", equalTo("gameId")).
		        body("url", equalTo("http://localhost/games/gameId"));
	}

    @Test
	public void games_gameId_pits_pitId() {

    	given().
                contentType(ContentType.JSON).
		when().
		        put("/games/gameId/pits/1").
		then().
		        statusCode(200).
		        body("id", equalTo("gameId")).
		        body("url", equalTo("http://localhost/games/gameId")).
		        body("status.1", equalTo("1")).
    	        body("status.2", equalTo("2")).
    	        body("status.5", equalTo("5"));
	}
}
