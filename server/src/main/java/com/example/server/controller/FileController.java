package com.example.server.controller;

import com.example.server.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

	private final FileStorageService fileService;

	@Autowired
	public FileController(FileStorageService fileService) {
		this.fileService = fileService;
	}

	@PostMapping(value = "/api/data")
	@ResponseStatus(HttpStatus.OK)
	public void handleDataUpload(@RequestParam("file") MultipartFile file) throws IOException {
		fileService.storeFile(file);
	}

	@PostMapping(value = "/api/script")
	@ResponseStatus(HttpStatus.OK)
	public void handleScriptUpload(@RequestParam("file") MultipartFile file) throws IOException {
		fileService.storeFile(file);
	}

}
