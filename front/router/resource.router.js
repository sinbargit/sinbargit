const express = require('express');
const path = require('path');
const router = express.Router();
const logger = require('log4js').getLogger("/resource");

router.get('/:page/:type/:name',function (req, resp, next) {
    logger.info("through route"+req.originalUrl);
    let fileName = req.params.name;
    let fileType = req.params.type;
    let filePage = req.params.page;
    let contentType = '';
    switch(path.extname(fileName))
    {
        case '.js': contentType='text/javascript;charset=utf-8';
            break;
        case '.css': contentType='text/css;charset=utf-8';
            break;
    }
    resp.setHeader('Content-Type',contentType);
    resp.sendFile(path.resolve(__dirname + '/../resource/'+filePage+'/'+fileType+'/'+fileName),function () {
        next();
    });
})

module.exports = router;
