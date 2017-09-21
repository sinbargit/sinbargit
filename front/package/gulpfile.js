const gulp = require('gulp');
const path = require('path');
const fs = require('fs');
const head = require('./gulpheader');
const foot = require('./gulpfoot');
const appRootDir = require('app-root-dir').get();
const rename = require("gulp-rename");
const gulpclean = require("gulp-clean");
const inject = require('gulp-inject-string');
const template = require('gulp-template');
const config = require('../config');
const uglify = require('gulp-uglify');
const cleanCSS = require('gulp-clean-css');
const babel = require('gulp-babel');

let sourcemaps = require('gulp-sourcemaps');

let compile = {cdn:config.cdn};

/**
 * head other clean:server端打包
 * compressCss compressJs 客户端压缩
 * */
gulp.task('default',["clean","head","other","compressCss","compressJs"],function () {});

gulp.task('compressCss',() => {
    const url = path.join(appRootDir,"/resource/**/css/");
    let stream = gulp.src([url+'*.css',"!"+url+"*.min.css"],{base: "./"});
    if(process.env.NODE_ENV !== "production")
    {
        stream = stream.pipe(sourcemaps.init()).pipe(cleanCSS()).pipe(sourcemaps.write());
    }
    else
    {
        stream = stream.pipe(cleanCSS())
    }
    return stream.pipe(rename({
        suffix: '.min'
    })).pipe(gulp.dest('./'));
});

gulp.task('compressJs',() => {
    const url = path.join(appRootDir,"/resource/**/js/");
    let stream = gulp.src([url+'*.js',"!"+url+"*.min.js"],{base: "./"}).pipe(babel({
        presets: ['env']
    }));
    if(process.env.NODE_ENV !== "production")
    {
        stream = stream.pipe(sourcemaps.init()).pipe(uglify()).pipe(sourcemaps.write());
    }
    else
    {
        stream = stream.pipe(uglify())
    }
    return stream.pipe(rename({
        suffix: '.min'
    })).pipe(gulp.dest('./'));
});

gulp.task('head',["clean"], function(cb) {
    return fs.readdir(path.join(appRootDir,"/template/page"),(error,list)=>{
        if (error) throw error;
        if(null!==list)
        {
            let count = 0;
            list.forEach((v,_,arr)=>{
                let pageDir = path.join(appRootDir,"template/page",v,"html/body.tpl");
                gulp.src(pageDir).pipe(rename(v+'.tpl')).pipe(head(v)).pipe(gulp.dest(path.join(appRootDir,"template/dest"))).on('finish',function () {
                    ++count === arr.length&&cb()
                });
            })
        }
    })
});
gulp.task('other',["head"], function(cb) {
    return fs.readdir(path.join(appRootDir,"/template/dest"),(error,list)=>{
        if (error) throw error;
        if(null!==list)
        {
            let count = 0;
            list.forEach((v,_,arr)=>{
                let pageDir = path.join(appRootDir,"template/dest",v);
                gulp.src(pageDir,{base:'./'}).pipe(foot(v.replace(/.tpl$/,""))).pipe(gulp.dest('./')).on('finish',function () {
                    gulp.src(pageDir,{base:'./'}).pipe(inject.wrap('<html>\n','</html>')).pipe(template(compile)).pipe(rename({extname:'.html'})).pipe(gulp.dest('./')).on('finish',function () {
                        gulp.src(pageDir,{read: false}).pipe(gulpclean({force: true})).on('finish',()=>{
                            ++count === arr.length&&cb()
                        })
                    })
                });
            })
        }
    })
});
gulp.task('clean', function() {
    return gulp.src(path.join(appRootDir,"template/dest/*"),{read: false}).pipe(gulpclean({force: true}));
})
