package com.dynatrace.microservices.remoting;

import org.springframework.web.client.RestClientException;

public interface ExceptionHandler {

	void handle(RestClientException e);
}
