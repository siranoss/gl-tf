package com.example.server.controller;

import com.example.server.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.*;
// import org.json.*;
// import java.util.Iterator;
// import java.util.Map;

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

	@PostMapping(value = "/api/run")
	//@CrossOrigin(origins = "http://localhost:4200")
	@CrossOrigin(origins = "*")
	@ResponseStatus(HttpStatus.OK)
	public String handleRun(@RequestBody String json) throws InterruptedException, IOException {
		//fileService.storeFile(file);

		//JSONObject jsonTree = new JSONObject(json);

		String args = "";
		String [] listArgs;
		String s = "";
		String tmp;
		// String args = "";

		System.out.println(json);

		//System.out.println(json);
		tmp = json.split(",")[0];
		args = json.split(",",2)[1];
		args = args.replace("[[", "").replace("]]}", "");
		args = args.replace("\"", "");
		//System.out.println("Args.split(':')"+args);
		args = args.replace("\"\"", "");
		args = args.split(":")[1];
		listArgs = args.split(",");
		args = "";
		for (String value: listArgs){
			args = args + "./storage/" + value + " ";
		}
		args = args + "BLOCKINGPOINT";
		//System.out.println(listArgs);
		tmp = tmp.split(":")[1];
		scriptName = tmp.substring(1, tmp.length()-1);


		//System.out.println("ScriptName:"+scriptName);
		if (scriptName != null) {
			try {
					String cmd = "python" + " ./storage/" + scriptName + " " + args ;

					Process p = Runtime.getRuntime().exec(cmd);
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

					while ((tmp = stdInput.readLine()) != null) {
							s = s + "<br>" + tmp;
							System.out.println(tmp);
					}

					s = s + "// IMPORTANT SEPARATOR //";

					while ((tmp = stdError.readLine()) != null) {
							s = s + "<br>" + tmp;
							System.out.println(tmp);
					}



					return s;
				}

			catch (IOException e) {
					System.out.println("exception happened - here's what I know: ");
					e.printStackTrace();
					s = "An error occured while running the script.";

			}

		}
		//	return "Error, no script provided.";
		return "An error occured, no script provided.";
	}
}
