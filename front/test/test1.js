let through = require( 'through2' );
let gutil = require( 'gulp-util' );
let path = require('path');
let fs = require( 'fs' );
let appRootDir = require('app-root-dir').get();
fs.readdir(path.join(appRootDir,"/template/page"),(error,list)=>{
    if (error) throw error;
    if(null!==list)
    {
        list.forEach((v)=>{
            let cssDir = path.join(appRootDir,"resource",v,"css/page.css");
            let pageDir = path.join(appRootDir,"template/page",v,"html/main.art");
            if(fs.exists(pageDir)&&fs.exists(cssDir)){

            }
        })
    }
})