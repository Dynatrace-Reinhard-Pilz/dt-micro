package com.dynatrace.microservices.remoting.documents;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paragraph")
@XmlAccessorType(XmlAccessType.FIELD)
public class Paragraph {

	@XmlAttribute(name = "id")
	private String id = null;
	
	@XmlAttribute(name = "contents")
	private String contents = null;
	
	public static Paragraph[] create(int size, int count) {
		if (count <= 0) {
			count = 1;
		}
		Paragraph[] paragraphs = new Paragraph[count];
		for (int i = 0; i < count; i++) {
			paragraphs[i] = Paragraph.create(size / count);
		}
		return paragraphs;
	}
	
	public static Paragraph create(int size) {
		return create(UUID.randomUUID().toString(), size);
	}
	
	public static Paragraph create(String id, int size) {
		Paragraph paragraph = new Paragraph();
		paragraph.setId(id);
		paragraph.setContents(Lorem.ipsum(size));
		return paragraph;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public int size() {
		return contents.length();
	}
	
	@Override
	public String toString() {
		return contents;
	}
}
