package com.dynatrace.microservices;

import java.util.UUID;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.processes.ServiceLauncher;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.registry.DefaultService;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.registry.ServiceInstanceRegistry;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.LocalRemoteRegistryService;
import com.dynatrace.microservices.remoting.RemoteRegistryService;

@SpringBootApplication
public class ServiceApplication {
	
	private static ApplicationContext ctx = null;
	private static final ServiceInstanceRegistry serviceRegistry = new ServiceInstanceRegistry();
	
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		ctx = SpringApplication.run(ServiceApplication.class, args);
		String serviceId = ctx.getEnvironment().getProperty("micro.service");
		int serverPort = Integer.parseInt(ctx.getEnvironment().getProperty("server.port"));
		
		Location thisLocation = new DefaultLocation("localhost", serverPort);
		Location registryLocation = RegistryConfig.parse(
			ctx.getEnvironment().getProperty("micro.registry"),
			thisLocation
		);
		Service localRegistryService = new DefaultService("registry", new Version(1, 0, 0, 0));
		Service service = new DefaultService(serviceId, new Version(1, 0, 0, 0));
		DefaultServiceInstance registryInstance = new DefaultServiceInstance(
			UUID.randomUUID().toString(),
			localRegistryService,
			registryLocation
		);
		DefaultServiceInstance thisInstance = new DefaultServiceInstance(
			UUID.randomUUID().toString(),
			service,
			thisLocation
		);
		RemoteRegistryService remoteRegistryService = new RemoteRegistryService(registryInstance);
		LocalRemoteRegistryService registryService = new LocalRemoteRegistryService(remoteRegistryService, new ExceptionHandler() {
			
			@Override
			public void handle(RestClientException e) {
				e.printStackTrace(System.err);
			}
		});
		registryService.register(thisInstance);
		if ("registry".equals(serviceId)) {
			ServiceLauncher serviceLauncher = new ServiceLauncher();
			serviceLauncher.setProperty("micro.service", "foo");
			serviceLauncher.setProperty("server.port", String.valueOf(serverPort + 1));
			serviceLauncher.setProperty("micro.registry", "localhost:" + serverPort);
			serviceLauncher.execute();
		}

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
