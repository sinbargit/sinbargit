const redis = require("../service/redis");
const config = require("../config");
const logger = require('log4js').getLogger("service delay");

class Delay {
    constructor(duration) {
        this.duration = duration;
        this.hit = true;
    }

    doneA(data = null) {
        if(this.hit)
        {
            clearTimeout(this.timer);
            this.callback(data, true);
        }
    }

    doneB(data = null) {
        this.callback(data, false);
    }

    begin() {
        this.planA(this.doneA);
        this.timer = setTimeout(() => {
            this.hit = false;
            this.planB(this.doneB);
        }, this.duration);
    }
    callback() {
    }
    planA() {
    }
    planB() {
    }
}

module.exports = class HitCache extends Delay {
    constructor(key,resp) {
        super(config.redis.timeout);
        this.key = key;
        this.resp = resp;
    }
    immediately()
    {
        clearTimeout(this.timer);
        this.hit = false;
        this.planB(this.doneB);
    }
    callback(data,_)
    {
        this.resp.send(data);
    }
    planA() {
        redis.get(this.key, (err, reply) => {
            if (null!=reply) {
                logger.info("use cache " + this.key);
                this.doneA(reply);
            }
            else {
                logger.info("miss cache " + this.key);
                this.immediately();
            }
        })
    }
}
