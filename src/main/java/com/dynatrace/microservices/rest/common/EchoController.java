package com.dynatrace.microservices.rest.common;

import org.springframework.web.bind.annotation.RestController;

@RestController("echo")
public class EchoController implements EchoService {

	@Override
	public String echo(String message) {
		return message;
	}

}
