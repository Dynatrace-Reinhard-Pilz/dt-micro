package com.dynatrace.microservices.rest.controller;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.processes.LaunchedServices;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.remoting.controller.ControllerService;
import com.dynatrace.microservices.utils.net.SimpleHostNameResolver;

@RestController("controller")
@ConditionalOnExpression("'${micro.service}'.equals('controller')")
public class ControllerController implements ControllerService {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(ControllerController.class.getName());
	
	private @Autowired HttpServletRequest request;
//	private @Autowired JdbcTemplate jdbcTemplate;
//	
//	private static class Quote {
//		
//		public int id = 0;
//		public String name = null;
//		
//		public Quote(int id, String name) {
//			this.id = id;
//			this.name = name;
//		}
//
//		@Override
//		public String toString() {
//			return id + " - " + name;
//		}
//	}
//	
//	@GetMapping(path = "/test")
//	public String test() {
//		jdbcTemplate.query(
//                "SELECT id, name FROM quote", (rs, rowNum) -> new Quote(rs.getInt("id"), rs.getString("name"))
//        ).forEach(quote -> LOGGER.info(quote.toString()));
//		return UUID.randomUUID().toString();
//	}
	
	@Override
	public ServiceInstance launch(@RequestBody LaunchConfig launchConfig) {
		Future<ServiceInstance> future = ServiceApplication.launchedServices.scheduleLaunch(launchConfig);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ServiceInstance register(@RequestBody ServiceInstance instance, @RequestParam(name = "correct", defaultValue = "false", required = false) boolean correct) {
		Objects.requireNonNull(instance);
		String instanceId = instance.getId();
		LaunchedServices launchedServices = ServiceApplication.launchedServices;
		ServiceInstance serviceInstance = launchedServices.get(instanceId);
		synchronized (serviceInstance) {
			if (correct) {
				serviceInstance.setLocation(
					new DefaultLocation(
						SimpleHostNameResolver.INSTANCE.getHostName(request),
						instance.getLocation().getPort()
					)
				);
			} else {
				serviceInstance.setLocation(instance.getLocation());
			}
			serviceInstance.notifyAll();
		}
		return serviceInstance;
	}

}