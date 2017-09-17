let through = require('through2');
let fs = require('fs');
let gulp = require('gulp');

let i = 1;
fs.createReadStream('cccc.mkv')
    .pipe(through(function (chunk, enc, callback) {
        this.push(chunk)
        callback()
    })).pipe(through(function (chunk, enc, callback) {
    console.log('yy' + i++);
    callback()
})).on('finish', function () {
        console.log('finish' + i);
    })