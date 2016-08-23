package com.dynatrace.microservices.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "success")
@XmlAccessorType(XmlAccessType.FIELD)
public class Success {
	
	public static final Success TRUE = new Success(true);
	public static final Success FALSE = new Success(false);

	@XmlAttribute(name = "success")
	private boolean success = false;
	
	public Success() {
		
	}
	
	public Success(boolean success) {
		this.success = success;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(success);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (success ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Success other = (Success) obj;
		if (success != other.success)
			return false;
		return true;
	}
	
}
