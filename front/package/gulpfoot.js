let gulp = require('gulp');
let path = require('path');
let fs = require('fs');
let appRootDir = require('app-root-dir').get();
let inject = require('gulp-inject-string');
let config = require('../config')

module.exports = function (page) {
    'use strict';

    let commonFootDir = path.join(appRootDir, "template", 'common', "html/footer.tpl");
    let footDir = path.join(appRootDir, "template/page", page, "html/footer.tpl");
    let foot = '\n'+fs.readFileSync(commonFootDir)+fs.readFileSync(footDir)+'\n';
    return inject.append(foot);
}
