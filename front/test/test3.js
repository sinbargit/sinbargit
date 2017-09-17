let through = require('through2');
let fs = require('fs');
let gulp = require('gulp');
let path = require('path');

gulp.task('default', [], function () {
    let i = 1;
    let j = 1;
    gulp.src(path.join(__dirname, './test/cccc.mkv'))
        .pipe(through.obj(function (chunk, enc, callback) {
            console.log(i++);
            this.push(chunk)
            callback()
        })).pipe(through.obj(function (chunk, enc, callback) {
        console.log(j++);
        callback()
    })).on('finish', function () {
        console.log('finished' + i);
    })
})
