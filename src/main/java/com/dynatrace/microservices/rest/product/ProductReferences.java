package com.dynatrace.microservices.rest.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "productrefs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductReferences implements Iterable<ProductReference> {
	
	@XmlElement(type=ProductReference.class, name = "productref")
    private Collection<ProductReference> products = new ArrayList<ProductReference>();

	
	public Collection<ProductReference> getProducts() {
		return products;
	}
	
	public void setProducts(Collection<ProductReference> products) {
		this.products = products;
	}
	
	public void add(ProductReference product) {
		this.products.add(product);
	}

	@Override
	public Iterator<ProductReference> iterator() {
		return new ArrayList<ProductReference>(products).iterator();
	}
	
}
