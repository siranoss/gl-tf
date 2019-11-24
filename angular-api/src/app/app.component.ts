import { NgModule, Component, ElementRef, OnInit, ViewChild, Injectable } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { HttpClient } from '@angular/common/http'
import 'rxjs/add/operator/map';

const axios = require('axios');

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  @ViewChild('dataInput', {static: false}) dataInput: ElementRef;
  @ViewChild('scriptInput', {static: false}) scriptInput: ElementRef;

  dataUploader: FileUploader;
  scriptUploader: FileUploader;
  dataIsDropOver: boolean;
  scriptIsDropOver: boolean;
  products = [];

  constructor(private runScriptGetRequest: HttpClient) { }

  ngOnInit(): void {
    const headers = [{name: 'Accept', value: 'application/json'}];
      this.dataUploader = new FileUploader({url: 'api/data', autoUpload: false, headers: headers });
      this.dataUploader.onCompleteAll = () => alert('File uploaded');
      this.scriptUploader = new FileUploader({url: 'api/script', autoUpload: false, headers: headers });
      this.scriptUploader.onCompleteAll = () => alert('File uploaded');
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

  runScript() {
    return this.runScriptGetRequest.get('http://localhost:8080/api/run').subscribe((response: any) => {
      return response;
    });
  }

  runScript2() {
    var script = this.scriptUploader.queue[0];
    var data = this.scriptUploader.queue;
    var listDataFiles = []
    for (let dataFile of data) {
      listDataFiles.push(dataFile.file.name);
    }
    var jsonToPost = {
      scriptName: script.file.name,
      dataList: [
        listDataFiles
      ]
    }
    return this.runScriptGetRequest.post('http://localhost:8080/api/run', jsonToPost).subscribe((response: any) => {
      return response;
    });
  }
}
