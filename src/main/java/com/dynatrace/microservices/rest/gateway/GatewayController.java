package com.dynatrace.microservices.rest.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.operation.Operation;

@RestController("gateway")
@ConditionalOnExpression("'${micro.service}'.equals('gateway')")
public class GatewayController {
	
	@Autowired
	ServiceProperties props;
	
	@PostMapping(path = "/process", consumes="application/json", produces="application/json")
	public RedirectView process(@RequestBody Operation operation) {
		// return operation.execute();
		return null;
	}

}