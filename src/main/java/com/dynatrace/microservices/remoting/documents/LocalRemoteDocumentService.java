package com.dynatrace.microservices.remoting.documents;

public class LocalRemoteDocumentService implements DocumentService {
	
	private final RemoteDocumentService service;
	
	public LocalRemoteDocumentService(RemoteDocumentService service) {
		this.service = service;
	}

	@Override
	public DocumentRef create(String name) {
		return service.create(name);
	}

	@Override
	public Document process(Document document) {
		return service.process(document);
	}

	@Override
	public Document transform(Document document) {
		return service.transform(document);
	}

	@Override
	public Document sign(Document document) {
		return service.sign(document);
	}

	@Override
	public Document ship(Document document) {
		return service.ship(document);
	}

	@Override
	public Paragraph encrypt(Paragraph paragraph) {
		return service.encrypt(paragraph);
	}

}
