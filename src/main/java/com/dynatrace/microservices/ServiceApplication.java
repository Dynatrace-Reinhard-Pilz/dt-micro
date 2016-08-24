package com.dynatrace.microservices;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.dynatrace.microservices.registry.ServiceInstanceRegistry;
import com.dynatrace.microservices.remoting.registry.LocalRemoteRegistryService;
import com.dynatrace.microservices.remoting.registry.RemoteRegistryService;

@SpringBootApplication
public class ServiceApplication {
	
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(ServiceApplication.class.getName());
	
	private static ApplicationContext ctx = null;
	private static final ServiceInstanceRegistry serviceRegistry = new ServiceInstanceRegistry();
	public static RemoteRegistryService remoteRegistryService = null;
	public static LocalRemoteRegistryService registryService = null;
	
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		ctx = SpringApplication.run(ServiceApplication.class, args);
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
