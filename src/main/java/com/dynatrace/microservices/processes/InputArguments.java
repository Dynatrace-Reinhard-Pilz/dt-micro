package com.dynatrace.microservices.processes;

import java.lang.management.ManagementFactory;
import java.util.List;

public class InputArguments {

	private final String[] args;
	
	public InputArguments() {
		List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
		this.args = args.toArray(new String[args.size()]);
	}
	
	public String toJvmArg() {
		if (args == null) {
			return "";
		}
		if (args.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			if (arg.startsWith("-Dcom.sun.management.jmxremote.port")) {
				continue;
			}
			if (arg.startsWith("-agentlib")) {
				continue;
			}
			sb.append(" ").append(arg);
		}
		return sb.toString();
	}
}
