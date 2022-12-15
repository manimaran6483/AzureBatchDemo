package com.azure.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.vm.service.VMProvisionService;
import com.azure.webapp.WebAppAzure;

@RestController
public class VMProvisionController {

	@Autowired
	private VMProvisionService vmProvisionService;
	
	private WebAppAzure webapp = new WebAppAzure();
	
	
	@GetMapping("/provision/vm")
	public ResponseEntity<Object> provisionVM(){
		boolean status = vmProvisionService.provisionVM();
		String msg = status ? "VM Provisioned Successfully" : "Error in provisioning VM";
		
		return new ResponseEntity<Object>(msg, status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/provision/webapp")
	public ResponseEntity<Object> createWebApp(){
		boolean status = webapp.createWebApp();
		String msg = status ? "WebApp created Successfully" : "Error in creating WebApp";
		
		return new ResponseEntity<Object>(msg, status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
