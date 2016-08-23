package com.dynatrace.microservices.processes;

import java.io.IOException;

import com.dynatrace.microservices.utils.OS;

public final class ServiceLauncher {
	
	private final InputArguments inputArguments = new InputArguments();
	private final CommandLine commandLine = new CommandLine();
	private final ClassPath classPath = new ClassPath(commandLine.first());

	public ServiceLauncher() {
		
	}
	
	public void setProperty(String key, String value) {
		commandLine.setProperty(key, value);
	}
	
	public void execute() {
		StringBuilder sb = new StringBuilder();
		sb.append('"').append(OS.javaExec().getAbsolutePath()).append('"');
		sb.append(classPath.toJvmArg());
		sb.append(inputArguments.toJvmArg());
		sb.append(commandLine.toJvmArg());
		System.err.println(sb.toString());
		try {
			Runtime.getRuntime().exec(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
