package com.dynatrace.microservices.processes;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.common.LocalRemoteCommonService;
import com.dynatrace.microservices.remoting.common.RemoteCommonService;
import com.dynatrace.microservices.rest.controller.LaunchConfig;

public final class LaunchedServices implements ThreadFactory {

	private final HashMap<String, InstanceProcess> instances = new HashMap<String, InstanceProcess>();
	private final ServiceInstance thisInstance;
	
	private final ExecutorService executor = Executors.newCachedThreadPool(this);
	
	private static final class InstanceProcess {
		public ServiceInstance instance = null;
		public Process process = null;
		
		public InstanceProcess(ServiceInstance instance, Process process) {
			Objects.requireNonNull(instance);
			Objects.requireNonNull(process);
			this.instance = instance;
			this.process = process;
		}
		
		public String getId() {
			return instance.getId();
		}
	}
	
	public LaunchedServices(ServiceInstance thisInstance) {
		this.thisInstance = thisInstance;
	}
	
	void register(InstanceProcess instanceProcess) {
		Objects.requireNonNull(instanceProcess);
		synchronized (instances) {
			instances.put(instanceProcess.getId(), instanceProcess);
		}
	}
	
	public void launch(LaunchConfig launchConfig) {
		Objects.requireNonNull(launchConfig);
		executor.submit(new InstanceCreation(launchConfig, this));
	}
	
	public Future<ServiceInstance> scheduleLaunch(LaunchConfig launchConfig) {
		Objects.requireNonNull(launchConfig);
		return executor.submit(new InstanceCreation(launchConfig, this));
	}
	
	public ServiceInstance get(String instanceId) {
		Objects.requireNonNull(instanceId);
		if (instanceId.equals(thisInstance.getId())) {
			return thisInstance;
		}
		synchronized (instances) {
			InstanceProcess process = instances.get(instanceId);
			if (process == null) {
				return null;
			}
			return process.instance;
		}
	}
	
	public void shutdown() {
		synchronized(instances) {
			for (InstanceProcess process : instances.values()) {
				if (process == null) {
					continue;
				}
				executor.execute(new InstanceShutdown(process));
			}
		}
		executor.shutdown();
	}
	
	@Override
	public Thread newThread(Runnable runnable) {
		Objects.requireNonNull(runnable);
		Thread thread = new Thread(runnable, LaunchedServices.class.getSimpleName());
		thread.setDaemon(true);
		return thread;
	}
	
	private static class InstanceCreation implements Callable<ServiceInstance> {
		
		private final LaunchConfig launchConfig;
		private final LaunchedServices launchedServices;
		
		public InstanceCreation(LaunchConfig launchConfig, LaunchedServices launchedServices) {
			Objects.requireNonNull(launchConfig);
			Objects.requireNonNull(launchedServices);
			this.launchConfig = launchConfig;
			this.launchedServices = launchedServices;
		}
		
		@Override
		public ServiceInstance call() throws Exception {
			ServiceLauncher serviceLauncher = new ServiceLauncher();
			ServiceInstance instance = serviceLauncher.prepare(launchConfig);
			String registryInstanceId = launchConfig.getRegistryInstanceId();
			ServiceInstance registryInstance = launchedServices.get(registryInstanceId);
			synchronized (registryInstance) {
				int registryPort = registryInstance.getLocation().getPort();
				while (registryPort == 0) {
					try {
						registryInstance.wait(1000);
					} catch (InterruptedException e) {
						return null;
					}
					registryPort = registryInstance.getLocation().getPort();				}
			}
			serviceLauncher.setProperty("micro.registry", launchedServices.get(registryInstanceId).getLocation().toString());
			Process process = serviceLauncher.execute();
			InstanceProcess instanceProcess = new InstanceProcess(instance, process);
			launchedServices.register(instanceProcess);
			return instance;
		}
	}
	
	private static class InstanceShutdown implements Runnable, ExceptionHandler {
		
		private final InstanceProcess instanceProcess;
		
		public InstanceShutdown(InstanceProcess instanceProcess) {
			Objects.requireNonNull(instanceProcess);
			this.instanceProcess = instanceProcess;
		}

		@Override
		public void run() {
			try {
				int port = instanceProcess.instance.getLocation().getPort();
				if (port != 0) {
					RemoteCommonService remoteCommonService = new RemoteCommonService(instanceProcess.instance);
					LocalRemoteCommonService commonService = new LocalRemoteCommonService(remoteCommonService, this);
					commonService.shutdown();
				} else {
					instanceProcess.process.destroy();
				}
			} catch (Throwable t) {
				t.printStackTrace(System.err);
			}
		}

		@Override
		public void handle(RestClientException e) {
			e.printStackTrace(System.err);
		}
		
	}
	
}
