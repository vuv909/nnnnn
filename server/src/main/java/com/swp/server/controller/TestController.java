package com.swp.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swp.server.dto.ProfileDTO;

@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/view")
	public ResponseEntity<?> view() {
		return ResponseEntity.ok("Test successful");
	}
	
}
