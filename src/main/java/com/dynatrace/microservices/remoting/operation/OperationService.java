package com.dynatrace.microservices.remoting.operation;

import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;

public interface OperationService {

	OperationResponse process(Operation operation);
	
}
