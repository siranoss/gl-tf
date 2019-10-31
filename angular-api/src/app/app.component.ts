import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';

const axios = require('axios');

interface HTMLInputEvent extends Event {
    target: HTMLInputElement & EventTarget;
}


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements AfterViewInit{
  title = 'angular-api';
  @ViewChild('pickerData',{static:false}) pickerData = ElementRef;
  @ViewChild('pickerScript',{static:false}) pickerScript = ElementRef;
  evtData : HTMLInputEvent;
  evtScript : HTMLInputEvent;

  ngOnInit() {}

  ngAfterViewInit() {}

  onFileChangeData(event) {
    this.evtData = event;
  }

  onFileChangeScript(event) {
    this.evtScript = event;
  }

  uploadData() {

    const data = new FormData();
    for(var x = 0; x<this.evtData.target.files.length; x++) {
      data.append('file', this.evtData.target.files[x]);
      console.log(this.evtData.target.files[x]);
    }
    axios.post("http://localhost:8000/upload", data, {
    })
    .then(res => {
      console.log(res.statusText);
    });
  }

  uploadScript() {

    const data = new FormData();
    for(var x = 0; x<this.evtScript.target.files.length; x++) {
      data.append('file', this.evtScript.target.files[x]);
    }
    axios.post("http://localhost:8000/upload", data, {
    })
    .then(res => {
      console.log(res.statusText);
    });
  }
}
