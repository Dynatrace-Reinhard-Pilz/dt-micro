package com.dynatrace.microservices.remoting.documents;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentRef {
	
	@XmlAttribute(name = "name")
	private String name = null;
	
	@XmlAttribute(name = "size")
	private int size = 0;
	
	public DocumentRef() {
		
	}
	
	public DocumentRef(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

}
