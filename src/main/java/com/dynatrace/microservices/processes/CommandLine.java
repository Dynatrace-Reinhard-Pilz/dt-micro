package com.dynatrace.microservices.processes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class CommandLine {

	private final String[] parts;
	private final Map<String, String> properties = new HashMap<String, String>();
	
	public CommandLine() {
		String commandLine = System.getProperty("sun.java.command");
		ArrayList<String> parts = new ArrayList<String>();
		StringTokenizer strTok = new StringTokenizer(commandLine, " ");
		while (strTok.hasMoreTokens()) {
			String part = strTok.nextToken().trim();
			if (!part.isEmpty()) {
				parts.add(part);
			}
		}
		if (parts.isEmpty()) {
			this.parts = new String[0];
		} else {
			this.parts = parts.toArray(new String[parts.size()]);
		}
	}
	
	public void setProperty(String key, String value) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		properties.put(key, value);
	}
	
	public String first() {
		if (parts == null) {
			return null;
		}
		if (parts.length == 0) {
			return null;
		}
		return parts[0];
	}
	
	public String toJvmArg() {
		if (parts == null) {
			return "";
		}
		if (parts.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (parts[0].endsWith(".jar")) {
			sb.append(" -jar");
		}
		for (String part : parts) {
			sb.append(" ").append(propify(part));
		}
		return sb.toString();
	}
	
	private String propify(String part) {
		Objects.requireNonNull(part);
		if (!part.startsWith("--")) {
			return part;
		}
		int idx = part.indexOf('=');
		String key = part.substring(2, idx);
		String property = properties.get(key);
		if (property == null) {
			return part;
		}
		return "--" + key + "=" + property;
	}
}
