package com.dynatrace.microservices.remoting.product;

import com.dynatrace.microservices.rest.product.Price;
import com.dynatrace.microservices.rest.product.Product;
import com.dynatrace.microservices.rest.product.ProductReferences;

public class LocalRemoteProductService implements ProductService {
	
	private final RemoteProductService service;
	
	public LocalRemoteProductService(RemoteProductService service) {
		this.service = service;
	}

	@Override
	public ProductReferences getProducts() {
		return service.getProducts();
	}

	@Override
	public Product getProduct(String productId) {
		return service.getProduct(productId);
	}

	@Override
	public Price getPrice(String productId) {
		return service.getPrice(productId);
	}

}
