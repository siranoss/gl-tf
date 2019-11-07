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

  upload(source) {
    var evt;
    if (source == "data")
      evt = this.evtData;
    else 
      evt = this.evtScript;
    const data = new FormData();
    for(var x = 0; x<evt.target.files.length; x++) {
      data.append('file', evt.target.files[x]);
      console.log(evt.target.files[x]);
    }
    axios.post("http://localhost:8000/upload", data, {
    })
    .then(res => {
      console.log(res.statusText);
    });
  }
}
