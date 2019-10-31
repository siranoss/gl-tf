const express = require('express');
const app = express();
const multer = require('multer');
const cors = require('cors');
const fs = require('fs');

app.use(cors());

const exec = require('child_process').exec;

var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "recep/");
  },
  filename: function (req, file, cb) {
    let filename = file.originalname;
    cb(null, filename);
  }
})

var upload = multer({ storage: storage , preservePath: true }).array('file');

app.post('/upload',function(req, res) {
  upload(req, res, function (err) {
    if (err instanceof multer.MulterError) {
      return res.status(500).json(err);
    }
    else if (err) {
      return res.status(500).json(err);
    }
    return res.status(200).send(req.file);
  })
});

app.get('/search/:query', function(req, res) {
  console.log(req.params.query);
});

app.listen(8000, function() {
  console.log('App running on port 8000');
});
