package com.dynatrace.microservices.remoting.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.rest.controller.LaunchConfig;

public interface ControllerService {

	@PostMapping(path = "/launch", consumes="application/json", produces="application/json")
	ServiceInstance launch(@RequestBody LaunchConfig launchConfig);
	
	@PostMapping(path = "/register", consumes="application/json", produces="application/json")
	ServiceInstance register(@RequestBody ServiceInstance instance, @RequestParam(name = "correct", defaultValue = "false", required = false) boolean correct);
	
}
