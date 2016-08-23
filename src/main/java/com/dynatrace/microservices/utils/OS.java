package com.dynatrace.microservices.utils;

import java.io.File;

public final class OS {

	private OS() {
		
	}
	
	private static String OS = null;
	
	public static String getOsName() {
		if(OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}
	
	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}
	
	public static String delim() {
		if (isWindows()) {
			return ";";
		}
		return ":";
	}
	
	public static boolean isLinux() {
		return !isWindows();
	}
	
	public static String java() {
		if (isWindows()) {
			return "javaw.exe";
		}
		return "java";
	}
	
	public static File javaExec() {
		String javaHome = System.getProperty("java.home");
		File f = new File(javaHome);
		f = new File(f, "bin");
		return new File(f, java());
	}
}
