package com.pamesh.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pamesh.rest.util.ApplicationProperties;

@RestController
public class MyRestController {

	@Autowired
	private ApplicationProperties properties;

	@GetMapping("/hello")
	public ResponseEntity<String> hello(){
		return new ResponseEntity<String>("Hi !!!"+ properties.getGreeting(), HttpStatus.OK);
	}
}
