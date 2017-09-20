const fs = require('fs');
const express = require('express');
const helmet = require('helmet');
const compression = require('compression');
const reactView = require('express-react-views')
//HTTP LOG
const httpLog = require('morgan');
const bodyParser = require('body-parser');
const config = require("./config");
const router = require('./router/router');
//SERVER LOG
const log4js = require('log4js');
const logger = log4js.getLogger("index");

log4js.configure({
    appenders: {
        out: { type: 'stdout' },
    },
    categories: {
        default: { appenders: [ 'out'], level: config.log }
    }
});

const app = express();
app.use(helmet());
app.use(compression());
app.use(httpLog('dev'));
app.use(bodyParser.json({
    limit: '10MB'
}));

app.use(router);

if(config.https.enable)
{
    const port = config.https.port||8443;
    const options = {
        key:fs.readFileSync(config.https.options.key),
        cert:fs.readFileSync(config.https.options.cert)
    }
    require('https').createServer(options,app).listen(port,()=>{
        logger.info('app listening on https on port '+port);
    })
}
else
{
    const port = config.port||8000;
    require('http').createServer(app).listen(port,()=>{
        logger.info('app listening on http on port '+port);
    })
}
