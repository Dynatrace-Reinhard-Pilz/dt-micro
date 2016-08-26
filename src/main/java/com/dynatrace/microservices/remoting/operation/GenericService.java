package com.dynatrace.microservices.remoting.operation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;

public interface GenericService {

	@PostMapping(path = "/${micro.service}/process", consumes="application/json", produces="application/json")
	OperationResponse process(@RequestBody Operation operation);
	
}
