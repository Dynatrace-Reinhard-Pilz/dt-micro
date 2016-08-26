package com.dynatrace.microservices.rest.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;
import com.dynatrace.microservices.remoting.operation.GenericService;

@RestController("operation")
@ConditionalOnExpression("!'${micro.service}'.equals('registry') && !'${micro.service}'.equals('gateway') && !'${micro.service}'.equals('controller')")
public class GenericController implements GenericService {
	
	@Autowired
	ServiceProperties props;
	
	@Override
	public OperationResponse process(@RequestBody Operation operation) {
		return operation.execute();
	}

}