const express = require('express');
const path = require('path');
const mapping = require('./mapping.json');
const EventsSet = require('../service/eventsset');
const template = require('art-template');
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
    const htmlPath = __dirname + '/../template/dest/' + name + '.tpl';

    const hisCache = new HisCache(name,resp);
    hisCache.planB = function()
    {
        let data;
        let self = this;
        events.add('getData');
        require(modelPath)(req, resp).then(function (d) {
            data = d;
            events.emit('getData');
        });
        events.done(function () {
            logger.info("use new composed page " + name);
            const html = template(htmlPath, data);
            self.doneB(html);
            redis.set(name, html, 'EX', config.redis.expire / 1000);
        });
    }
    hisCache.begin();
})
module.exports = router;

