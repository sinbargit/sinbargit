let gulp = require('gulp');
let path = require('path');
let fs = require('fs');
let appRootDir = require('app-root-dir').get();
let inject = require('gulp-inject-string');
let config = require('../config')

module.exports = function (page) {
    'use strict';

    let commonHeadDir = path.join(appRootDir, "template", 'common', "html/head.tpl");
    let headDir = path.join(appRootDir, "template/page", page, "html/head.tpl");
    let head = '<head>\n'+(config[page]?'':fs.readFileSync(commonHeadDir))+fs.readFileSync(headDir)+'</head>\n';
    return inject.prepend(head);
}
