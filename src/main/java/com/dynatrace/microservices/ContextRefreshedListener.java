package com.dynatrace.microservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.dynatrace.microservices.infrastructure.DefaultServices;
import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.processes.LaunchedServices;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.registry.DefaultService;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.remoting.registry.LocalRemoteRegistryService;
import com.dynatrace.microservices.remoting.registry.RemoteRegistryService;
import com.dynatrace.microservices.rest.controller.LaunchConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ContextRefreshedListener implements ApplicationListener<ApplicationReadyEvent> {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(ContextRefreshedListener.class.getName());
	
	public void onApplicationEvent(ApplicationReadyEvent event) {
		ApplicationContext ctx = event.getApplicationContext();
		
		String serviceId = ctx.getEnvironment().getProperty("micro.service");
		int serverPort = Integer.parseInt(ctx.getEnvironment().getProperty("local.server.port"));
		
		String instanceId = null;
		String configuredInstanceId = ctx.getEnvironment().getProperty("micro.instance");
		if (configuredInstanceId != null) {
			instanceId = configuredInstanceId;
		} else {
			instanceId = UUID.randomUUID().toString();
		}
		
		
		Location thisLocation = new DefaultLocation("localhost", serverPort);
		Service thisService = new DefaultService(serviceId, Version.DEFAULT);
		DefaultServiceInstance thisInstance = new DefaultServiceInstance(instanceId, thisService, thisLocation);
		ServiceApplication.setInstance(thisInstance);
		ServiceApplication.launchedServices = new LaunchedServices(thisInstance);
		
		Location registryLocation = RegistryConfig.parse(
			ctx.getEnvironment().getProperty("micro.registry")
		);
		
		ServiceInstance registryServiceInstance = null;
		
		if (DefaultServices.controller.matches(ServiceApplication.getInstance())) {
			Future<ServiceInstance> future = ServiceApplication.launchedServices.scheduleLaunch(
				LaunchConfig.get(
					DefaultServices.registry.name(),
					"localhost",
					0,
					ServiceApplication.getInstance().getId()
				)
			);
			try {
				registryServiceInstance = future.get();
			} catch (InterruptedException | ExecutionException e) {
				return;
			}
			try (InputStream in = getClass().getClassLoader().getResourceAsStream("instances.json")) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					InstancesConfig instancesConfig = objectMapper.readValue(in, InstancesConfig.class);
					Collection<String> instances = instancesConfig.getInstances();
					for (String instance : instances) {
						ServiceApplication.launchedServices.launch(
								LaunchConfig.get(
									instance,
									"localhost",
									0,
									registryServiceInstance.getId()
								)
							);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace(System.err);
			}
		} else {
			LOGGER.info("Registry Location: " + registryLocation);
			Service localRegistryService = new DefaultService(DefaultServices.registry.name(), Version.DEFAULT);
			registryServiceInstance = new DefaultServiceInstance(
					UUID.randomUUID().toString(),
					localRegistryService,
					registryLocation
				);
			RemoteRegistryService remoteRegistryService = new RemoteRegistryService(registryServiceInstance);
			ServiceApplication.setRegistryService(new LocalRemoteRegistryService(remoteRegistryService));
		}
	}

}
