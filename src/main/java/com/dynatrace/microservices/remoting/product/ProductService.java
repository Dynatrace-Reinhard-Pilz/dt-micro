package com.dynatrace.microservices.remoting.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dynatrace.microservices.rest.product.Price;
import com.dynatrace.microservices.rest.product.Product;
import com.dynatrace.microservices.rest.product.ProductReferences;

public interface ProductService {

	@GetMapping(path = "/${micro.service}/products", produces="application/json")
	ProductReferences getProducts();
	
	@GetMapping(path = "/${micro.service}/products/{productId}", produces="application/json")
	Product getProduct(@PathVariable String productId);
	
	@GetMapping(path = "/${micro.service}/products/{productId}/price", produces="application/json")
	Price getPrice(@PathVariable String productId);
	
}
