package com.dynatrace.microservices.remoting.operation;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.operation.Operation;
import com.dynatrace.microservices.operation.OperationResponse;

public class RemoteOperationService {
	
	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteOperationService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}

	public OperationResponse process(Operation operation) throws RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Operation> entity = new HttpEntity<Operation>(operation, headers);
		ResponseEntity<OperationResponse> responseInstance = REST.postForEntity(
			serviceInstance.createURL("process").toString(),
			entity,
			OperationResponse.class
		);
		return responseInstance.getBody();
	}

}
