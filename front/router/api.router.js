const express = require('express');
const fs = require('fs');
const path = require('path');
const router = express.Router();
const EventsSet = require('../service/eventsset');
const logger = require('log4js').getLogger("/api");
const config = require("../config");
const redis = require("../service/redis");
const HisCache = require('../service/delay');

module.exports = router;

const events = new EventsSet();
const basePath = path.join(__dirname,'/../template/api');
const api = fs.readdirSync(basePath);
router.use('/:name',(req,resp,next) => {
    const name = req.params.name;
    logger.info("through route "+name);
    if(!api.includes(name))
    {
        resp.sendStatus(404);
    }
    else
    {
        const key = '/api/'+name;
        const hisCache = new HisCache(key,resp);
        hisCache.planB = function()
        {
            let data;
            let self = this;
            events.add('getData');
            require(path.join(basePath,name))(req,resp).then(function (d) {
                data=d;
                events.emit('getData');
            });
            events.done(function () {
                const data = JSON.stringify(data);
                self.done(data);
                redis.set(key, data, 'EX', config.redis.expire / 1000);
            });
        }
        hisCache.begin();
    }
})