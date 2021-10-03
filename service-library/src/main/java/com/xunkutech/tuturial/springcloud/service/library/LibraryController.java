package com.xunkutech.tuturial.springcloud.service.library;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LibraryController {

	@GetMapping("/libraries")
	public ResponseEntity<Map<String, String>> getLibraryList(){ 
		return ResponseEntity.ok(LibraryService.getLibList());	
	}
	
}
