package com.dynatrace.microservices.remoting.documents;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dynatrace.microservices.infrastructure.ServiceInstance;

public class RemoteDocumentService {
	
	private static final RestTemplate REST = new RestTemplate();
	
	private final ServiceInstance serviceInstance;
	
	public RemoteDocumentService(ServiceInstance serviceInstance) {
		Objects.requireNonNull(serviceInstance);
		this.serviceInstance = serviceInstance;
	}

	public DocumentRef create(String name) throws RestClientException {
		String url = serviceInstance.createURL("create/{name}", true).toString();
		return REST.getForObject(url, DocumentRef.class, name);
	}

	public Document process(Document document) throws RestClientException {
		return call(document, "processdoc");
	}
	
	public Document transform(Document document) throws RestClientException {
		return call(document, "transform");
	}

	public Document sign(Document document) throws RestClientException {
		return call(document, "sign");
	}

	public Document ship(Document document) throws RestClientException {
		return call(document, "ship");
	}
	
	public Paragraph encrypt(Paragraph paragraph) throws RestClientException {
		return call(paragraph, "encrypt");
	}
	
	private Document call(Document document, String operation) throws RestClientException {
		Objects.requireNonNull(document);
		Objects.requireNonNull(operation);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Document> entity = new HttpEntity<Document>(document, headers);
		ResponseEntity<Document> responseInstance = REST.postForEntity(
			serviceInstance.createURL(operation, true).toString(),
			entity,
			Document.class
		);
		return responseInstance.getBody();
	}
	
	private Paragraph call(Paragraph paragraph, String operation) throws RestClientException {
		Objects.requireNonNull(paragraph);
		Objects.requireNonNull(operation);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Paragraph> entity = new HttpEntity<Paragraph>(paragraph, headers);
		ResponseEntity<Paragraph> responseInstance = REST.postForEntity(
			serviceInstance.createURL(operation, true).toString(),
			entity,
			Paragraph.class
		);
		return responseInstance.getBody();
	}
	

}
