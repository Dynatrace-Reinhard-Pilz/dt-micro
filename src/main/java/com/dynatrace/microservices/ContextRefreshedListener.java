package com.dynatrace.microservices;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.Service;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.processes.NodeNameRegistry;
import com.dynatrace.microservices.processes.ServiceLauncher;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.registry.DefaultService;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.registry.LocalRemoteRegistryService;
import com.dynatrace.microservices.remoting.registry.RemoteRegistryService;
import com.dynatrace.microservices.utils.net.PortUtil;

@Component
public class ContextRefreshedListener implements ApplicationListener<ApplicationReadyEvent> {

	private static final Log LOGGER = LogFactory.getLog(ContextRefreshedListener.class.getName());
	
	public void onApplicationEvent(ApplicationReadyEvent event) {
		LOGGER.info("-------------- " + event.getClass().getName() + " received");
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
			instanceId,
			service,
			thisLocation
		);
		ServiceApplication.remoteRegistryService = new RemoteRegistryService(registryInstance);
		ServiceApplication.registryService = new LocalRemoteRegistryService(ServiceApplication.remoteRegistryService, new ExceptionHandler() {
			
			@Override
			public void handle(RestClientException e) {
				e.printStackTrace(System.err);
			}
		});
		ServiceApplication.registryService.register(thisInstance);
		if ("registry".equals(serviceId)) {
			for (int i = 0; i < 2; i++) {
				ServiceLauncher serviceLauncher = new ServiceLauncher();
				String childServiceName = "foo";
				serviceLauncher.setCellName(childServiceName);
				serviceLauncher.setProperty("micro.service", childServiceName);
				serviceLauncher.setProperty("server.port", String.valueOf(PortUtil.getFreePort()));
				serviceLauncher.setProperty("micro.registry", "localhost:" + serverPort);
				String childInstanceId = String.valueOf(NodeNameRegistry.get("global")) + "-" + String.valueOf(NodeNameRegistry.get(childServiceName));
				serviceLauncher.setNodeName(childInstanceId);
				serviceLauncher.setProperty("micro.instance", childInstanceId);
				serviceLauncher.setProperty("root.level.console", "OFF");
				serviceLauncher.execute();
			}
		}
	}
}
