import { NgModule, Component, ElementRef, OnInit, ViewChild, Injectable } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { HttpClient } from '@angular/common/http'
import 'rxjs/add/operator/map';
import { FormBuilder, FormGroup, FormControl, FormArray } from '@angular/forms';

const axios = require('axios');

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    // resultTemplate: `<div [innerHTML]="resultValue"></div>`,
    // errorTemplate: `<div [innerHTML]="errorValue"></div>`,
})
export class AppComponent implements OnInit {

    @ViewChild('dataInput', { static: false }) dataInput: ElementRef;
    @ViewChild('scriptInput', { static: false }) scriptInput: ElementRef;
    @ViewChild('importInput', { static: false }) importInput: ElementRef;

    editorOptionsCode = { theme: 'vs-dark', language: 'python' };
    editorOptionsImport = { theme: 'vs-dark', language: 'python' };
    code: string = 'def max(a, b):\n\tif (a > b):\n\t\treturn a\n\telse:\n\t\treturn b\n\nprint(max(1, 3))\n';
    import: string = '';

    runIndication: String;
    resultValue: String;
    resultHidder: boolean;
    errorValue: String;
    errorHidder: boolean;
    dataUploader: FileUploader;
    scriptUploader: FileUploader;
    importUploader: FileUploader;
    dataIsDropOver: boolean;
    scriptIsDropOver: boolean;
    products = [];
    fileForm: FormGroup;
    listFiles = [];
    filesToSend: Array<string> = [];
    //filesToSend = [];

    constructor(private runScriptGetRequest: HttpClient) {
    }

    ngOnInit(): void {
        this.runIndication = "Waiting for a script to run";
        this.resultHidder = true;
        this.errorHidder = true;
        const headers = [{ name: 'Accept', value: 'application/json' }];
        this.dataUploader = new FileUploader({ url: 'api/data', autoUpload: true, headers: headers });
        /*this.dataUploader.onCompleteAll = () => alert('File uploaded');*/
        this.scriptUploader = new FileUploader({ url: 'api/script', autoUpload: true, headers: headers });
        this.importUploader = new FileUploader({ url: 'api/import', autoUpload: true, headers: headers });
        /* this.scriptUploader.onCompleteAll = () => {
             alert('File uploaded');
         }*/
        this.scriptUploader.response.subscribe(res => this.code = res);
        this.importUploader.response.subscribe(res => {
            this.import = res;//[0].importFile;
            console.log(res);
        });
    }

    displayer() {
        this.resultHidder = false;
        this.errorHidder = false;
    }

    dataOverAnother(e: any): void {
        this.dataIsDropOver = e;
    }

    dataClicked() {
        this.dataInput.nativeElement.click();
    }

    scriptOverAnother(e: any): void {
        this.scriptIsDropOver = e;
    }

    scriptClicked() {
        this.scriptInput.nativeElement.click();
    }

    importClicked() {
        this.importInput.nativeElement.click();
    }

    fillList() {
        var data = this.dataUploader.queue;
        for (let dataFile of data) {
            this.listFiles.push(dataFile.file.name);
        }
        console.log(this.listFiles.length);
    }

    submitFiles(i: number) {
        if (!this.listFiles.length) {
            this.fillList();
        }
        console.log("Ok " + this.listFiles[i]);
        var indexFile = this.filesToSend.indexOf(this.listFiles[i]);
        console.log("index: " + indexFile);
        if(indexFile == -1){
          this.filesToSend.push(this.listFiles[i]);
        }
        else{
          this.filesToSend.splice(indexFile, 1);
        }
        console.log(this.filesToSend.length);
        //  this.filesToSend.push(this.listFiles[i]);
    }

    runScript() {
        var script = this.scriptUploader.queue[0];
        var jsonToPost = {
            scriptName: script.file.name,
            dataList: this.filesToSend
        }
        this.listFiles = [];
        this.filesToSend = [];
        this.dataUploader.queue = [];
        this.scriptUploader.queue = [];

        console.log(jsonToPost);
        this.runScriptGetRequest.post('http://localhost:8080/api/run', jsonToPost).subscribe((response) => {
            console.log(response);

            var responseScriptResult = response[0].stdIn;
            var responseScriptError = response[0].stdEr;

            if(response[0].retType == 1){
              this.runIndication = "An error occured!"
            }
            else if(response[0].retType == 0){
              this.runIndication = "Execution succeed";
            }

            this.resultValue = responseScriptResult;
            this.errorValue = responseScriptError;
        });
    }

    uploadWrittenScript() {
        let file = new File(this.code.split(""), "_pythonScript.py", { type: "text/plain" });
        let files = [file];
        this.scriptUploader.addToQueue(files);
    }

    uploadWrittenImport() {
        let file = new File(this.import.split(""), "_pythonImport.py", { type: "text/plain" });
        let files = [file];
        this.importUploader.addToQueue(files);
    }
}
