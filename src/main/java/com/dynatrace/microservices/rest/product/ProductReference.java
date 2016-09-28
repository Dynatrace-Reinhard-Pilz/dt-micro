package com.dynatrace.microservices.rest.product;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "productref")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductReference {

	@XmlAttribute(name = "id")
	private int id = 0;
	
	public ProductReference() {
		
	}
	
	public ProductReference(int id) {
		this.id = id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
