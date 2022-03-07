package com.poc.es.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poc.es.model.ResponseObject;
import com.poc.es.service.IAppService;
import com.poc.es.utils.AppServiceException;

@RestController
@RequestMapping("/es")
public class AppController {

	@Autowired
	private IAppService service;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/index")
	public ResponseEntity<ResponseObject> index(@RequestParam String name, @RequestParam MultipartFile document,
			@RequestParam String id) {
		ResponseObject resp = new ResponseObject();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand("index").toUri();
		try {
			String msg = service.index(name, document, id);
			resp.setMessage(msg);
			resp.setStatusCode(HttpStatus.OK);
		} catch (AppServiceException e) {
			e.printStackTrace();
			resp.setMessage(e.getMessage() + ":: " + e.getCause());
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.created(location).body(resp);
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseObject> search(@RequestParam String key) {
		ResponseObject resp = new ResponseObject();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand("search").toUri();
		try {
			String msg = service.search(key);
			resp.setMessage(msg);
			resp.setStatusCode(HttpStatus.OK);
		} catch (AppServiceException e) {
			e.printStackTrace();
			resp.setMessage(e.getMessage() + ":: " + e.getCause());
			resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.created(location).body(resp);
	}
}
