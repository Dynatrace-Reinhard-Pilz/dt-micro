package com.dynatrace.microservices.rest.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;

@RestController("operation")
@ConditionalOnExpression("!'${micro.service}'.equals('registry')")
public class OperationController {
	
	@Autowired
	ServiceProperties props;
	
	@PostMapping(path = "/${micro.service}/process", consumes="application/json", produces="application/json")
	public OperationResponse process(@RequestBody Operation operation) {
		return operation.execute();
	}

}