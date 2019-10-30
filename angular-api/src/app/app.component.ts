import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'angular-api';

  uploadScript() {
    /*let pickerScript = document.getElementById('pickerScript');
    var evtScript;
    pickerScript.addEventListener('change', e => {
      for (let file of Array.from(e.target.files)) {
        let item = document.createElement('li');
        item.textContent = file.webkitRelativePath;
      };
      evtScript = e;
    });

    const data = new FormData();
    for(var x = 0; x<evtScript.target.files.length; x++) {
      data.append('file', evtScript.target.files[x]);
    }
    axios.post("Bend address", data, {
    })
    .then(res => {
      console.log(res.statusText);
    });*/
  }

  uploadData() {
    /*let pickerData = document.getElementById('pickerData');
    var evtData;
    pickerData.addEventListener('change', e => {
      for (let file of Array.from(e.target.files)) {
        let item = document.createElement('li');
        item.textContent = file.webkitRelativePath;
      };
      evtData = e;
    });

    const data = new FormData();
    for(var x = 0; x<evtData.target.files.length; x++) {
      data.append('file', evtData.target.files[x]);
    }
    axios.post("Bend address", data, {
    })
    .then(res => {
      console.log(res.statusText);
    });*/
  }
}