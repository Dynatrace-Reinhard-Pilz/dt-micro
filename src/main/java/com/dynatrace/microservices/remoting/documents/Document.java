package com.dynatrace.microservices.remoting.documents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dynatrace.microservices.operation.Operation;

@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

	@XmlAttribute(name = "name")
	private String name = null;
	
	@XmlAttribute(name = "summary")
	private String summary = null;
	
	@XmlElement(type=Operation.class, name = "paragraphs", required = false)
    private Collection<Paragraph> paragraphs = new ArrayList<Paragraph>();
	
	public Document() {
	}

	public Document(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}
	
	public Document(String name, int size) {
		Objects.requireNonNull(name);
		this.name = name;
		this.summary = Lorem.ipsum(size);
	}
	
	public void add(Paragraph[] paragraphs) {
		if (paragraphs == null) {
			return;
		}
		for (Paragraph paragraph : paragraphs) {
			add(paragraph);
		}
	}
	
	public void add(Paragraph paragraph) {
		if (paragraph == null) {
			return;
		}
		paragraphs.add(paragraph);
	}
	
	public Collection<Paragraph> getParagraphs() {
		return paragraphs;
	}
	
	public void setParagraphs(Collection<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public int size() {
		return summary == null ? 0 : summary.length();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (summary != null) {
			sb.append(summary).append("\r\n");
		}
		if (paragraphs != null) {
			for (Paragraph paragraph : paragraphs) {
				if (paragraph != null) {
					sb.append(paragraph.toString()).append("\r\n");
				}
			}
		}
		return sb.toString();
	}
}
