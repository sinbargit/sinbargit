const redis = require("redis");
const config = require("../config");
const logger = require('log4js').getLogger("service redis");

if(config.redis&&config.redis.connect)
{
    let redisClient = redis.createClient(config.redis.connect);
    redisClient.on("error", function (err) {
        logger.error(err);
    });
    module.exports = redisClient;
}
else
{
    logger.warn("no redis config");
}

