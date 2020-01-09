package com.example.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.io.FileReader;

import com.example.server.service.FileStorageService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void CheckForImport(String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("./storage/" + fileName));
        String st;
        Process p;
        String[] libs;
        String[] cmd = { "pip", "instal", "" };

        System.out.println("Checking for imports");
        while ((st = br.readLine()) != null) {
            if (st.contains("import")) {
                libs = st.split(" ");
                System.out.println("installing the lib:" + libs[1]);
                cmd[2] = libs[1];
                p = Runtime.getRuntime().exec(cmd);
            }
        }
        br.close();
    }

    @Autowired
    public FileController(FileStorageService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/api/data")
    // @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.OK)
    public void handleDataUpload(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.storeFile(file);
    }

    @PostMapping(value = "/api/script")
    // @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.OK)
    public void handleScriptUpload(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.storeFile(file);
        scriptName = file.getOriginalFilename();
    }

    @PostMapping(value = "/api/run")
    // @CrossOrigin(origins = "http://localhost:4200")
    @CrossOrigin(origins = "*")
    // @ResponseStatus(HttpStatus.OK)
    public String handleRun(@RequestBody RequestPayload json) throws InterruptedException, IOException {
        // fileService.storeFile(file);
        String args = "";
        List<String> listArgs;
        String stdIn = "";
        String stdEr = "";
        String tmp;
        System.out.println(json.scriptName);

        // JSONObject jsonReceived = new JSONObject(json);
        // scriptName = jsonReceived.getString("scriptName");
        // JSONArray arr = jsonReceived.getJSONArray("dataList");
        // for (int i = 0; i < arr.length(); i++) {
        // args += arr.getJSONObject(i);
        // System.out.println(args);
        // }

        // s = jsonReceived.getJSONObject("script");

        /*
         * // System.out.println(json); tmp = json.split(",")[0]; args = json.split(",",
         * 2)[1]; args = args.replace("[", "").replace("]}", ""); args =
         * args.replace("\"", ""); // System.out.println("Args.split(':')"+args); args =
         * args.replace("\"\"", ""); args = args.split(":")[1];
         */
        listArgs = json.dataList;
        args = "";
        for (String value : listArgs) {
            args = args + "./storage/" + value + " ";

        }
        JSONObject obj = new JSONObject();
        JSONArray ret = new JSONArray();

        scriptName = json.scriptName;
        CheckForImport(scriptName);

        try {
            int retType = 0;
            //String[] cmd = { "python", "./storage/" + scriptName, args };
            String cmd = "python" + " ./storage/" + scriptName + " " + args;
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
