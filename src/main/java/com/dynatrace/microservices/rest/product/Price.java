package com.dynatrace.microservices.rest.product;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "price")
@XmlAccessorType(XmlAccessType.FIELD)
public class Price {

	public static final Price NOT_FOUND = new Price(-1);
	public static final Price ERROR = new Price(-2);
	
	private int value = 0;
	
	public Price() {
		
	}
	
	public Price(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
