const deep = require('deep-extend');
const appRootDir = require('app-root-dir').get();
const path = require('path');
const log4js = require('log4js');
const logger = log4js.getLogger("config");
/**
 * 时间单位毫秒
 * 深copy合并,使用dev覆盖production时必须指明一级属性
 * */
const production = {
    "port": 8000,
    //https
    "https": {
        "enable": false,
        "port": 443,
        "options": {
            "key": path.join(appRootDir, 'keystore/server.key'),
            "cert": path.join(appRootDir, 'keystore/server.crt')
        }
    },
    "cdn": "",
    "nonHead": [],
    "log": "WARN",
    "redis": {
        "connect": {
            "host": "10.77.43.22",
            "port": 6380,
            "password": "YxPtHd#$@qe23XAMD",
            "retry_strategy": function (options) {
                logger.warn("connect redis timeout... retrying...");
                if (options.error && options.error.code === 'ECONNREFUSED') {
                    return new Error('The server refused the connection');
                }
                if (options.total_retry_time > 1000 * 60 * 60) {
                    return new Error('Retry time exhausted');
                }
                if (options.attempt > 100) {
                    return new Error('Retry amount exhausted');
                }
                return 1000;
            }
        },
        //过期时间最少1s=1000ms
        "expire": 10000,
        //获取缓存时等待时间,等待时间后直接拼凑新的page
        "timeout": 500
    }
};
const dev = {
    "cdn": "//localhost:8000/resource/",
    "log": "INFO",
    "redis": {"expire": 10000, "timeout": 500}
};
let config = production;
if (process.env.NODE_ENV !== "production") {
    config = deep({}, production, dev)
}
module.exports = config;