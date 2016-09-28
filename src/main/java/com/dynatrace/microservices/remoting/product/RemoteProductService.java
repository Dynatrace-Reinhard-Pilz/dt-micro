package com.dynatrace.microservices.remoting.product;

import java.util.Objects;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.rest.product.Price;
import com.dynatrace.microservices.rest.product.Product;
import com.dynatrace.microservices.rest.product.ProductReferences;

public class RemoteProductService {
	
	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteProductService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}

	public ProductReferences getProducts() throws RestClientException {
		String url = serviceInstance.createURL("products", true).toString();
		return REST.getForObject(url, ProductReferences.class);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<Operation> entity = new HttpEntity<Operation>(operation, headers);
//		ResponseEntity<OperationResponse> responseInstance = REST.postForEntity(
//			serviceInstance.createURL("process").toString(),
//			entity,
//			OperationResponse.class
//		);
//		return responseInstance.getBody();
	}
	
	public Product getProduct(String productId) throws RestClientException {
		String url = serviceInstance.createURL("products/{productId}", true).toString();
		return REST.getForObject(url, Product.class, productId);
	}
	
	public Price getPrice(String productId) throws RestClientException {
		String url = serviceInstance.createURL("products/{productId}/price", true).toString();
		return REST.getForObject(url, Price.class, productId);
	}

}
