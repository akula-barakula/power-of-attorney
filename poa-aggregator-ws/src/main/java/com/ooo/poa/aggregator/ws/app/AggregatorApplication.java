package com.ooo.poa.aggregator.ws.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

// TODO OOO add HTTPS
@SpringBootApplication
@Import(AggregatorApplicationConfig.class)
public class AggregatorApplication {

    public static void main(String[] arguments) {

    	SpringApplication.run(AggregatorApplication.class, arguments);
    }
}
