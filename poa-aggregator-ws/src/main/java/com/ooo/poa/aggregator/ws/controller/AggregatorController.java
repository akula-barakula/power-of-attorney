package com.ooo.poa.aggregator.ws.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ooo.poa.aggregator.service.AggregatorService;
import com.ooo.poa.aggregator.ws.api.PowerOfAttorneyAggr;
import com.ooo.poa.aggregator.ws.api.AppStatusAggr;

@RestController
@RequestMapping("aggregator")
public class AggregatorController {

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private AggregatorService aggregatorService;


	@GetMapping(
			value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
	@Valid
	@ResponseBody
	public ResponseEntity<AppStatusAggr> status() {

		String message = String.format("%s is running. Current timestamp is %s", getClass().getName(), new Date());
		AppStatusAggr response = responseBuilder.buildAppStatus(message);

		return ResponseEntity.ok(response);
	}

	@GetMapping(
			value = "/power-of-attorneys",
            produces = MediaType.APPLICATION_JSON_VALUE)
	@Valid
	@ResponseBody
	public ResponseEntity<List<PowerOfAttorneyAggr>> getPowerOfAttorneys() {

		List<PowerOfAttorneyAggr> response = aggregatorService.getPowerOfAttorneys();

		return ResponseEntity.ok(response);
	}
}
