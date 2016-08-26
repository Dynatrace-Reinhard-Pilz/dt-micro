package com.dynatrace.microservices.processes;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.infrastructure.Location;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.registry.DefaultLocation;
import com.dynatrace.microservices.registry.DefaultService;
import com.dynatrace.microservices.registry.DefaultServiceInstance;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.common.LocalRemoteCommonService;
import com.dynatrace.microservices.remoting.common.RemoteCommonService;
import com.dynatrace.microservices.rest.controller.LaunchConfig;
import com.dynatrace.microservices.utils.OS;

public final class ServiceLauncher implements ExceptionHandler {
	
	private static final Log LOGGER = LogFactory.getLog(ServiceLauncher.class.getName());
	
//	private static final ArrayList<ServiceLauncher> LAUNCHERS = new ArrayList<ServiceLauncher>();
	
	private final InputArguments inputArguments = new InputArguments();
	private final CommandLine commandLine = new CommandLine();
	private final ClassPath classPath = new ClassPath(commandLine.first());
	private String nodeName = null;
	private String cellName = null;
	private String serverPort = null;
	private String serviceName = null;

	public ServiceLauncher() {
	}
	
	public ServiceInstance prepare(LaunchConfig launchConfig) {
		Objects.requireNonNull(launchConfig);
		
		String serviceName = launchConfig.getServiceName();
		Location location = launchConfig.getLocation();
		int listenPort = location.getPort();
		String registryInstanceId = launchConfig.getRegistryInstanceId();
		ServiceInstance registryInstance = ServiceApplication.launchedServices.get(registryInstanceId);
		Location registryLocation = registryInstance.getLocation();
		setCellName(serviceName);
		setProperty("micro.service", serviceName);
		setProperty("server.port", String.valueOf(listenPort));
		setProperty("micro.registry", registryLocation.toString());
		String childInstanceId = String.valueOf(NodeNameRegistry.get("global")) + "-" + String.valueOf(NodeNameRegistry.get(serviceName));
		setNodeName(childInstanceId);
		setProperty("micro.instance", childInstanceId);
		setProperty("root.level.console", "OFF");
		DefaultService service = new DefaultService(serviceName, Version.DEFAULT);
		return new DefaultServiceInstance(childInstanceId, service, location);
	}
	
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	
	public void setProperty(String key, String value) {
		commandLine.setProperty(key, value);
		if (key.equals("server.port")) {
			serverPort = value;
		}
		if (key.equals("micro.service")) {
			this.serviceName = value;
		}
	}
	
	public void shutdown() {
		DefaultService service = new DefaultService(serviceName, Version.DEFAULT);
		DefaultLocation location = new DefaultLocation("localhost", Integer.parseInt(serverPort));
		DefaultServiceInstance serviceInstance = new DefaultServiceInstance(UUID.randomUUID().toString(), service, location);
		RemoteCommonService remoteCommonService = new RemoteCommonService(serviceInstance);
		LocalRemoteCommonService lrcs = new LocalRemoteCommonService(remoteCommonService, this);
		lrcs.shutdown();
	}
	
//	public static void shutdownAll() {
//		for (ServiceLauncher serviceLauncher : LAUNCHERS) {
//			serviceLauncher.shutdown();
//		}
//	}
	
	public Process execute() {
		ArrayList<String> parts = new ArrayList<String>();
//		parts.add("\"" + OS.javaExec().getAbsolutePath() + "\"");
		parts.add(OS.javaExec().getAbsolutePath());
		parts.addAll(classPath.toJvmArg());
		parts.addAll(inputArguments.toJvmArg());
		parts.addAll(commandLine.toJvmArg());
		LOGGER.info(String.join(" ", parts));
		ProcessBuilder pb = new ProcessBuilder(parts);
		if (nodeName != null) {
			pb.environment().put("node_name", nodeName);
		}
		if (cellName != null) {
			pb.environment().put("cell_name", cellName);
		}
		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectInput(Redirect.INHERIT);
		try {
			return pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void handle(RestClientException e) {
		LOGGER.warn("remoting failed", e);
	}
	
	
}
