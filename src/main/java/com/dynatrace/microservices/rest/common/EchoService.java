package com.dynatrace.microservices.rest.common;

import org.springframework.web.bind.annotation.GetMapping;

public interface EchoService {

	@GetMapping(path = "/echo")
	String echo(String message);
	
}
