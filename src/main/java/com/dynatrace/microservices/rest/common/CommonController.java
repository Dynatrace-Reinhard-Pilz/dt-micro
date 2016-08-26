package com.dynatrace.microservices.rest.common;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.remoting.common.CommonService;

@RestController("common")
public class CommonController implements CommonService {
	
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
}