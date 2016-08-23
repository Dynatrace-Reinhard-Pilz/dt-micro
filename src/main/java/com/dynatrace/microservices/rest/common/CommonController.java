package com.dynatrace.microservices.rest.common;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceApplication;

@RestController("common")
public class CommonController {
	
	private static final Status STATUS = new Status(UUID.randomUUID().toString());
	
	@GetMapping(path = "/ping")
	public String ping() {
		return "pong";
	}
	
	@GetMapping(path = "/status")
	public Status getStatus() {
		return STATUS;
	}
	
	@GetMapping(path = "/shutdown")
	public String shutdown() {
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
		return Boolean.TRUE.toString();
	}
}