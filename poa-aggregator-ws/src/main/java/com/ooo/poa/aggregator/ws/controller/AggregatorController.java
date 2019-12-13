package com.ooo.poa.aggregator.ws.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.ws.model.AppStatusAggr;
import com.ooo.poa.aggregator.ws.model.PowerOfAttorneyAggr;

@RestController
@RequestMapping("/aggregator")
public class AggregatorController {

	@Autowired
	private AggregatorService aggregatorService;

	@Autowired
	private AggregatorExceptionHandler exceptionHandler;


	@GetMapping(
			value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
	@Valid
	@ResponseBody
	public ResponseEntity<AppStatusAggr> getStatus() {

	    AppStatusAggr response = new AppStatusAggr();
		String message = String.format("%s is running. Current timestamp is %s", getClass().getName(), new Date());

		response.setMessage(message);

		return ResponseEntity.ok(response);
	}

	@GetMapping(
			value = "/power-of-attorneys",
            produces = MediaType.APPLICATION_JSON_VALUE)
	@Valid
	@ResponseBody
	public ResponseEntity<List<PowerOfAttorneyAggr>> getPowerOfAttorneys(@RequestParam String user) {

		List<PowerOfAttorneyAggr> response = aggregatorService.getPowerOfAttorneys(user);

		return ResponseEntity.ok(response);
	}


    @ExceptionHandler
	public ResponseEntity<Object> handleException(Exception exception) {

    	HttpStatus status = exceptionHandler.toHttpStatus(exception);

        return ResponseEntity
        		.status(status)
        		.body(null);
    }
}
