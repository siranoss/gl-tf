package com.example.server.controller;

import com.example.server.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.*;

@RestController
public class FileController {

	private final FileStorageService fileService;
	private String scriptName = null;

	@Autowired
	public FileController(FileStorageService fileService) {
		this.fileService = fileService;
	}

	@PostMapping(value = "/api/data")
	//@CrossOrigin(origins = "http://localhost:4200")
	@ResponseStatus(HttpStatus.OK)
	public void handleDataUpload(@RequestParam("file") MultipartFile file) throws IOException {
		fileService.storeFile(file);
	}

	@PostMapping(value = "/api/script")
	//@CrossOrigin(origins = "http://localhost:4200")
	@ResponseStatus(HttpStatus.OK)
	public void handleScriptUpload(@RequestParam("file") MultipartFile file) throws IOException {
		fileService.storeFile(file);
		scriptName = file.getOriginalFilename();
	}

	@GetMapping(value = "/api/run")
	//@CrossOrigin(origins = "http://localhost:4200")
	@ResponseStatus(HttpStatus.OK)
	public String handleRun() throws InterruptedException {
		String s = "";
		String tmp;

		if (scriptName != null) {
			try {
					String[] cmd = { "python", "./storage/"+scriptName };
					Process p = Runtime.getRuntime().exec(cmd);
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

					while ((tmp = stdInput.readLine()) != null) {
							s = s + "\n" + tmp;
							System.out.println(tmp);
					}

					while ((tmp = stdError.readLine()) != null) {
							s = s + "\n" + tmp;
							System.out.println(tmp);
					}
			}
			catch (IOException e) {
					System.out.println("exception happened - here's what I know: ");
					e.printStackTrace();
					s = "An error occured while running the script.";
			}
			finally {
				return s;
			}
		}
		else {
			return "Error, no script provided.";
		}

	}
}
