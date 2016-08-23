package com.dynatrace.microservices.rest.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dynatrace.microservices.ServiceProperties;
import com.dynatrace.microservices.workflow.WorkflowStep;
import com.dynatrace.microservices.workflow.WorkflowStepResponse;

@RestController("workflow")
@ConditionalOnExpression("!'${micro.service}'.equals('registry')")
public class WorkflowController {
	
	@Autowired
	ServiceProperties props;
	
	@PostMapping(path = "/${micro.service}/process", consumes="application/json", produces="application/json")
	public WorkflowStepResponse process(@RequestBody WorkflowStep request) {
		return new WorkflowStepResponse();
	}

}