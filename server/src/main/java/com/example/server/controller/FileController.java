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
	private String scriptName;

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
		scriptName = file.getOriginalFilename;
	}

	@GetMapping(value = "/api/run")
	@ResponseStatus(HttpStatus.OK)
	public void handRun() throws InterruptedException {
		try {
			System.out.println("dÃ©but du programme");
			String[] cmd = { "python", "./storage/" + scriptName};
			Process p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fin du programme");
	}
}
