package com.dynatrace.microservices.processes;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.dynatrace.microservices.utils.OS;

public class ClassPath {

	private final String[] parts;
	
	public ClassPath(String exclude) {
		if (exclude == null) {
			exclude = "";
		}
		String cp = ManagementFactory.getRuntimeMXBean().getClassPath();
		ArrayList<String> parts = new ArrayList<String>();
		StringTokenizer strTok = new StringTokenizer(cp, OS.delim());
		while (strTok.hasMoreTokens()) {
			String part = strTok.nextToken().trim();
			if (part.isEmpty()) {
				continue;
			}
			if (exclude.equals(part)) {
				continue;
			}
			parts.add(part);
		}
		this.parts = parts.toArray(new String[parts.size()]);
	}
	
	public String toJvmArg() {
		if (parts == null) {
			return "";
		}
		if (parts.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" -classpath ");
		sb.append('"').append(parts[0]).append('"');
		String delim = OS.delim();
		for (int i = 1; i < parts.length; i++) {
			sb.append(delim).append('"').append(parts[i]).append('"');
		}
		return sb.toString();
	}
	
}
