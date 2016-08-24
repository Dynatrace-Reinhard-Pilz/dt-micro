package com.dynatrace.microservices.remoting.operation;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;
import com.dynatrace.microservices.remoting.ExceptionHandler;

public class LocalRemoteOperationService implements OperationService {
	
	private final RemoteOperationService service;
	private final ExceptionHandler exceptionHandler;
	
	public LocalRemoteOperationService(RemoteOperationService service, ExceptionHandler exceptionHandler) {
		this.service = service;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public OperationResponse process(Operation operation) {
		try {
			return service.process(operation);
		} catch (RestClientException e) {
			exceptionHandler.handle(e);
			return new OperationResponse(false);
		}
	}

}
