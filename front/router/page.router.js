const express = require('express');
const path = require('path');
const fs = require('fs');
const Vue = require('vue');
const renderer = require('vue-server-renderer').createRenderer();
const mapping = require('./mapping.json');
const EventsSet = require('../service/eventsset');
const config = require("../config");
const redis = require("../service/redis");
const logger = require('log4js').getLogger("/page");
const HisCache = require('../service/delay');
const router = express.Router();
const events = new EventsSet();

router.get("*", (req, resp, next) => {
    let name = mapping[req.originalUrl];
    logger.info("through route" + req.originalUrl);
    if (name === undefined) {
        resp.sendFile(path.join(__dirname, '/../resource/', req.originalUrl), function () {
            next();
        });
        return;
    }
    resp.setHeader('Content-Type', 'text/html');
    const modelPath = __dirname + '/../template/page/' + name + '/controller';
    const htmlPath = __dirname + '/../template/dest/' + name + '.html';

    const hisCache = new HisCache(name,resp);
    hisCache.planB = function()
    {
        let data;
        let html;
        let self = this;
        events.add('getData');
        events.add('getHtml');
        require(modelPath)(req, resp).then(function (d) {
            data = d;
            events.emit('getData');
        });
        fs.readFile(htmlPath,'utf8',(err,data)=>{
            if(err)
            {
                logger.error(err);
                resp.status(500).end(err);
            }
            else
            {
                html = data;
                events.emit('getHtml');
            }
        })
        events.done(function () {
            logger.info("use new composed page " + name);
            const app = new Vue({
                data:data,
                template: html
            });
            renderer.renderToString(app,(err,html)=>{
                if (err) {
                    logger.error(err);
                    resp.status(500).end(err);
                }
                else
                {
                    self.doneB('<!DOCTYPE html>'+html);
                    redis.set(name, html, 'EX', config.redis.expire / 1000);
                }
            });
        });
    }
    hisCache.begin();
})
module.exports = router;

