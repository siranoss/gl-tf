package com.example.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.example.server.service.FileStorageService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

	private final FileStorageService fileService;
	private String scriptName = null;

	public void ImportDependencies(String[] dependencies) throws IOException {

		Process p;
		String []cmd = { "pip", "instal", "" };
		String s = new String();
		String tmp;
		for(String dependency: dependencies){
			cmd[2] = dependency;
			p= Runtime.getRuntime().exec(cmd);
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

	}

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

	@PostMapping(value = "/api/import")
	//@CrossOrigin(origins = "http://localhost:4200")
	@CrossOrigin(origins = "*")
	//@ResponseStatus(HttpStatus.OK)
	public String handleImport(@RequestParam("file") MultipartFile file) throws InterruptedException, IOException {
		byte[] bytes = file.getBytes();
		String imports = new String(bytes);
		String[] dependencies = imports.split("\n");
		Process p;
		String []cmd = { "pip", "install", "" };
		String stdIn = "";
		String stdErr = "";
		String tmp;
		int retValue = 0;
		JSONObject obj = new JSONObject();
		JSONArray ret = new JSONArray();

		for(String dependency: dependencies){

			cmd[2] = dependency;
			p= Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			while ((tmp = stdInput.readLine()) != null) {
					stdIn = stdIn + "\n" + tmp;
					System.out.println(tmp);
			}

			while ((tmp = stdError.readLine()) != null) {
					retValue = 1;
					stdErr = stdErr + "\n" + tmp;
					System.out.println(tmp);
			}
		}

		obj.put("stdIn", stdIn);
		obj.put("stdEr", stdErr);
		obj.put("retType", retValue);
		ret.put(obj);

		return ret.toString();
	}


	@PostMapping(value = "/api/run")
	//@CrossOrigin(origins = "http://localhost:4200")
	@CrossOrigin(origins = "*")
	//@ResponseStatus(HttpStatus.OK)
		public String handleRun(@RequestBody RequestPayload json) throws InterruptedException, IOException {
			// fileService.storeFile(file);
			String args = "";
			List<String> listArgs;
			String stdIn = "";
			String stdEr = "";
			String tmp;
			System.out.println(json.scriptName);


			listArgs = json.dataList;
			args = "";
			for (String value : listArgs) {
				args = args + value + " ";

			}
			JSONObject obj = new JSONObject();
			JSONArray ret = new JSONArray();

			scriptName = json.scriptName;

			try {
				int retType = 0;
				String[] cmd = { "python", "./storage/" + scriptName, args };
				System.out.println("python ./storage/" + scriptName + " " + args);

				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				while ((tmp = stdInput.readLine()) != null) {
					stdIn = stdIn + "<br>" + tmp;
					System.out.println(tmp);
				}

				while ((tmp = stdError.readLine()) != null) {
					retType = 1;
					stdEr = stdEr + "<br>" + tmp;
					System.out.println(tmp);
				}

				obj.put("stdIn", stdIn);
				obj.put("stdEr", stdEr);
				obj.put("retType", retType);

				ret.put(obj);

				return ret.toString();
			}

			catch (IOException e) {
				System.out.println("exception happened - here's what I know: ");
				e.printStackTrace();

			}

			return "An error occured, no script provided.";

		}
}
