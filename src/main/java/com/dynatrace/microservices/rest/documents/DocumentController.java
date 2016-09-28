package com.dynatrace.microservices.rest.documents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.dynatrace.microservices.ServiceApplication;
import com.dynatrace.microservices.infrastructure.ServiceInstance;
import com.dynatrace.microservices.infrastructure.Version;
import com.dynatrace.microservices.registry.DefaultServiceQuery;
import com.dynatrace.microservices.remoting.ExceptionHandler;
import com.dynatrace.microservices.remoting.documents.Document;
import com.dynatrace.microservices.remoting.documents.DocumentRef;
import com.dynatrace.microservices.remoting.documents.DocumentService;
import com.dynatrace.microservices.remoting.documents.LocalRemoteDocumentService;
import com.dynatrace.microservices.remoting.documents.Paragraph;
import com.dynatrace.microservices.remoting.documents.RemoteDocumentService;

@RestController("documents")
public class DocumentController implements DocumentService, ExceptionHandler {
	
	private static final Log LOGGER = LogFactory.getLog(DocumentController.class.getName());
	
	private static final int START_SIZE = 1024 * 512;
	private static final Random RAND = new Random(System.currentTimeMillis());
	
	private DocumentService getService(String serviceId) {
		DefaultServiceQuery query = new DefaultServiceQuery(serviceId, Version.DEFAULT);
		ServiceInstance serviceInstance = ServiceApplication.getRegistryService().lookup(query).random();
		RemoteDocumentService remoteService = new RemoteDocumentService(serviceInstance);
		return new LocalRemoteDocumentService(remoteService);
	}

	@Override
	public void handle(RestClientException e) {
		LOGGER.warn("failed", e);
	}
	
	private Document enc(Document document) {
		if (true) {
			return document;
		}
		int cnt = document.size() % 5 + 10;
		for(int i = 0; i < cnt; i++) {
			document.add(getService("doc-enc").encrypt(Paragraph.create((document.size() % 10) + 10)));
		}
		return document;
	}
	
	public void accessDatabase() {
		try {
			DataSource dataSource = ServiceApplication.getDataSource();
			String table = "paragraph";
			int rnd = RAND.nextInt(30);
			String n = String.valueOf(rnd);
			if (rnd < 10) {
				n = "00" + n;
			} else if (rnd < 100) {
				n = "0" + n;
			}
			table = "paragraph" + n;
			try (
				Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE id = ?");
			) {
				statement.setInt(1, RAND.nextInt(1000));
				try (ResultSet rs = statement.executeQuery()) {
					if (!rs.next()) {
						return;
					}
				}
			}
		} catch (Throwable t) {
			LOGGER.warn("...failed", t);
		}
	}

	@Override
	@GetMapping(path = "/${micro.service}/create/{name}", produces="application/json")
	public DocumentRef create(@PathVariable String name) {
		int size = START_SIZE / 2 + RAND.nextInt(START_SIZE / 2);
		accessDatabase();
		Document document = new Document(name, size);
		accessDatabase();
		Document finished = getService("doc-proc").process(document);
		accessDatabase();
		enc(finished);
		finished = getService("doc-proc").process(finished);
		enc(finished);
		return new DocumentRef(finished.getName(), finished.getSummary().length());
	}
	
	private Document forward(Document document) {
		Objects.requireNonNull(document);
		int size = START_SIZE + RAND.nextInt(START_SIZE / 2);
		int numParagraphs = 3 + RAND.nextInt(7);
		accessDatabase();
		document.add(Paragraph.create(size, numParagraphs));
		return document; 
	}

	@Override
	@PostMapping(path = "/${micro.service}/processdoc", consumes="application/json", produces="application/json")
	public Document process(@RequestBody Document document) {
		accessDatabase();
		Document d = getService("doc-trans").transform(forward(document));
		enc(d);
		d = getService("doc-trans").transform(forward(d));
		enc(d);
		d = getService("doc-trans").transform(forward(d));
		enc(d);
		return d;
	}

	@Override
	@PostMapping(path = "/${micro.service}/transform", consumes="application/json", produces="application/json")
	public Document transform(@RequestBody Document document) {
		accessDatabase();
		return getService("doc-sign").sign(enc(forward(document)));
	}

	@Override
	@PostMapping(path = "/${micro.service}/sign", consumes="application/json", produces="application/json")
	public Document sign(@RequestBody Document document) {
		accessDatabase();
		accessDatabase();
		accessDatabase();
		return getService("doc-ship").ship(enc(forward(document)));
	}

	@Override
	@PostMapping(path = "/${micro.service}/ship", consumes="application/json", produces="application/json")
	public Document ship(@RequestBody Document document) {
		accessDatabase();
		return enc(forward(document));
	}

	@Override
	@PostMapping(path = "/${micro.service}/encrypt", consumes="application/json", produces="application/json")
	public Paragraph encrypt(@RequestBody Paragraph paragraph) {
		int enc = 0;
		char[] chars = paragraph.getContents().toCharArray();
		for (char c : chars) {
			for (int i = 1; i < c; i++) {
				enc = enc + RAND.nextInt(i);
			}
			enc = enc + c;
		}
		return Paragraph.create(paragraph.size());
	}

}
