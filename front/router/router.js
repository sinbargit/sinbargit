let express = require('express');
let pageRouter = require('./page.router');
let apiRouter = require('./api.router');
let resourceRouter = require('./resource.router');
const router = express.Router();

router.use('/api',apiRouter);
router.use('/resource',resourceRouter);
router.use(/^(?!\/?api$|\/?api\/.*|\/?resource$|\/?resource\/.*)/,pageRouter);
//router.use('/:page',pageRouter);
router.use(function (err,req, res, next) {
    console.error(err.stack);
    res.status(500).send('Something broke!');
})
module.exports=router;