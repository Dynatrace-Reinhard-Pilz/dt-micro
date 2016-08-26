package com.dynatrace.microservices;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.processes.LaunchedServices;
import com.dynatrace.microservices.registry.ServiceInstanceRegistry;
import com.dynatrace.microservices.registry.SynchronizedServiceInstance;
import com.dynatrace.microservices.remoting.registry.RegistryService;
import com.dynatrace.microservices.remoting.registry.SynchronizedRegistryService;

@SpringBootApplication
public class ServiceApplication {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(ServiceApplication.class.getName());
	
	private static ApplicationContext ctx = null;
	private static final ServiceInstanceRegistry serviceRegistry = new ServiceInstanceRegistry();
	private static SynchronizedRegistryService registryService = new SynchronizedRegistryService();
	public static RegistrationTask registrationTask = new RegistrationTask();
	private static SynchronizedServiceInstance thisInstance = new SynchronizedServiceInstance(ServiceInstance.UNDEFINED);
	public static LaunchedServices launchedServices = null;
	
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		ctx = SpringApplication.run(ServiceApplication.class, args);
	}
	
	public static void setRegistryService(RegistryService service) {
		Objects.requireNonNull(service);
		registryService.set(service);
	}
	
	public static RegistryService getRegistryService() {
		return registryService;
	}
	
	public static void setInstance(ServiceInstance instance) {
		Objects.requireNonNull(instance);
		thisInstance.set(instance);
	}
	
	public static ServiceInstance getInstance() {
		return thisInstance;
	}
	
	public static void shutdown() {
		SpringApplication.exit(ctx, new ExitCodeGenerator() {

			@Override
			public int getExitCode() {
				return 0;
			}
			
		});
	}
	
	public static ServiceInstanceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
}
