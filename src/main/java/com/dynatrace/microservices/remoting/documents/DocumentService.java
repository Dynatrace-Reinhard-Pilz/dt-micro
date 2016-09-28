package com.dynatrace.microservices.remoting.documents;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DocumentService {

	@GetMapping(path = "/${micro.service}/create", produces="application/json")
	DocumentRef create(@PathVariable String name);

	@PostMapping(path = "/${micro.service}/processdoc", consumes="application/json", produces="application/json")
	Document process(@RequestBody Document document);
	
	@PostMapping(path = "/${micro.service}/transform", consumes="application/json", produces="application/json")
	Document transform(@RequestBody Document document);

	@PostMapping(path = "/${micro.service}/sign", consumes="application/json", produces="application/json")
	Document sign(@RequestBody Document document);

	@PostMapping(path = "/${micro.service}/ship", consumes="application/json", produces="application/json")
	Document ship(@RequestBody Document document);
	
	@PostMapping(path = "/${micro.service}/encrypt", consumes="application/json", produces="application/json")
	Paragraph encrypt(@RequestBody Paragraph paragraph);
	
	
}
