package com.dynatrace.microservices.rest.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.registry.DefaultServiceQuery;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.common.CommonService;
import com.dynatrace.microservices.remoting.product.LocalRemoteProductService;
import com.dynatrace.microservices.remoting.product.ProductService;
import com.dynatrace.microservices.remoting.product.RemoteProductService;
import com.dynatrace.microservices.rest.product.ProductReference;
import com.dynatrace.microservices.rest.product.ProductReferences;

@RestController("common")
public class CommonController implements CommonService, ExceptionHandler {
	
	private static final Log LOGGER = LogFactory.getLog(CommonController.class.getName());
	
	private static final Status STATUS = new Status(UUID.randomUUID().toString());
	
	@Override
	public String ping() {
		return "pong";
	}
	
	@Override
	public Status getStatus() {
		return STATUS;
	}
	
	@Override
	public boolean shutdown() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				ServiceApplication.shutdown();
			}
		}.start();
		return true;
	}
	
	private void handleProducts() {
		ProductService productService = getProductService();
		ProductReferences products = productService.getProducts();
		for (ProductReference productReference : products) {
			int price = getPrice(productReference);
			// LOGGER.info(productReference.getId() + ": $" + price);
		}
	}
	
	private int getPrice(ProductReference product) {
		Objects.requireNonNull(product);
		ProductService productService = getProductService();
		return productService.getPrice(String.valueOf(product.getId())).getValue();
	}
	
	private ProductService getProductService() {
		DefaultServiceQuery query = new DefaultServiceQuery("products", Version.DEFAULT);
		ServiceInstance serviceInstance = ServiceApplication.getRegistryService().lookup(query).random();
		RemoteProductService remoteService = new RemoteProductService(serviceInstance);
		return new LocalRemoteProductService(remoteService);
	}
	
	@Override
	public String getQuote() {
		try {
			handleProducts();
			return Boolean.TRUE.toString();
		} catch (Throwable t) {
			try (
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos)
			) {
				t.printStackTrace(ps);
				return new String(baos.toByteArray());
			} catch (IOException ioe) {
				ioe.printStackTrace(System.err);
				return ioe.getMessage();
			}
		}
	}

	@Override
	public void handle(RestClientException e) {
	}
	
}