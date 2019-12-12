package com.example.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.server.service.FileStorageService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.json.*;

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
	//@ResponseStatus(HttpStatus.OK)
	public String handleRun(@RequestBody String json) throws InterruptedException, IOException {
		//fileService.storeFile(file);
		String args = "";
		String [] listArgs;
		String s = "";
		String tmp;
		System.out.println(json);

		JSONObject jsonReceived = new JSONObject(json);
		scriptName = jsonReceived.getString("scriptName");
		JSONArray arr = jsonReceived.getJSONArray("dataList");
		for (int i = 0; i < arr.length(); i++) {
            args +=  arr.getJSONObject(i);
            System.out.println(args);
        }
	//	s = jsonReceived.getJSONObject("script");

		//System.out.println(json);
		/*tmp = json.split(",")[0];
		args = json.split(",",2)[1];
		args = args.replace("[[", "").replace("]]}", "");
		args = args.replace("\"", "");
		//System.out.println("Args.split(':')"+args);
		args = args.replace("\"\"", "");
		args = args.split(":")[1];
		listArgs = args.split(",");
		args = "";
		for (String value: listArgs){
			args = args + value + " ";

		}
		//System.out.println(listArgs);
		tmp = tmp.split(":")[1];
		//scriptName = tmp.substring(1, tmp.length()-1);
		//System.out.println("ScriptName:"+scriptName);*/
		if (scriptName != null) {
			try {
					String[] cmd = { "python", "./storage/"+scriptName, args };

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
					return json;
				}

			catch (IOException e) {
					System.out.println("exception happened - here's what I know: ");
					e.printStackTrace();
					s = "An error occured while running the script.";

			}

		}
		else {
		//	return "Error, no script provided.";
		return json;
		}
		return json;

	}
}
