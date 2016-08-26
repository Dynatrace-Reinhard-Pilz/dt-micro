package com.dynatrace.microservices.remoting.common;

import org.springframework.web.bind.annotation.GetMapping;

import com.dynatrace.microservices.rest.common.Status;

public interface CommonService {

	@GetMapping(path = "/shutdown")
	boolean shutdown();
	
	@GetMapping(path = "/ping")
	public String ping();
	
	@GetMapping(path = "/status")
	public Status getStatus();

	
}
