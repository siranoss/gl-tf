import {Component, ElementRef, OnInit, ViewChild } from '@angular/core';
//import { HttpClient } from '@angular/common/http';
import {  FileUploader } from 'ng2-file-upload';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  @ViewChild('dataInput', {static: false}) dataInput: ElementRef;
  @ViewChild('scriptInput', {static: false}) scriptInput: ElementRef;

  dataUploader: FileUploader;
  scriptUploader: FileUploader;
  dataIsDropOver: boolean;
  scriptIsDropOver: boolean;


  ngOnInit(): void {
    const headers = [{name: 'Accept', value: 'application/json'}];
    this.dataUploader = new FileUploader({url: 'api/data', autoUpload: false, headers: headers});
    this.dataUploader.onCompleteAll = () => alert('File uploaded');
    this.scriptUploader = new FileUploader({url: 'api/script', autoUpload: false, headers: headers});
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
    if(!scriptIsDropOver) {
      alert('No script posted');
      return;
    }

  }
}
