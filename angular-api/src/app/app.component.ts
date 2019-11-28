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
})
export class AppComponent implements OnInit {

    @ViewChild('dataInput', { static: false }) dataInput: ElementRef;
    @ViewChild('scriptInput', { static: false }) scriptInput: ElementRef;
    @ViewChild('resultDisplayer', { static: false }) resultDisplayer: HTMLDivElement;
    @ViewChild('errorDisplayer', { static: false }) errorDisplayer: HTMLDivElement;

    dataUploader: FileUploader;
    scriptUploader: FileUploader;
    dataIsDropOver: boolean;
    scriptIsDropOver: boolean;
    products = [];
    fileForm: FormGroup;
    listFiles = [];
    filesToSend = [];

    constructor(private runScriptGetRequest: HttpClient) {
    }

    ngOnInit(): void {
        const headers = [{ name: 'Accept', value: 'application/json' }];
        this.dataUploader = new FileUploader({ url: 'api/data', autoUpload: false, headers: headers });
        this.dataUploader.onCompleteAll = () => alert('File uploaded');
        this.scriptUploader = new FileUploader({ url: 'api/script', autoUpload: false, headers: headers });
        this.scriptUploader.onCompleteAll = () => alert('File uploaded');
    }

    displayer() {
        resultDisplayer.hidden = false;
        errorDisplayer.hidden = false;
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
        this.filesToSend.push(this.listFiles[i]);
    }

    runScript() {
        var script = this.scriptUploader.queue[0];
        var jsonToPost = {
            scriptName: script.file.name,
            dataList: [
                this.filesToSend
            ]
        }

        console.log(jsonToPost.dataList[0]);
        this.runScriptGetRequest.post('http://localhost:8080/api/run', jsonToPost).subscribe((response) => {
            console.log(response);

            var responseScriptResult = response[0].stdIn;
            var responseScriptError = response[0].stdEr;

            resultDisplayer = responseScriptResult;
            errorDisplayer.innerHTML = responseScriptError;
        });
    }
}
