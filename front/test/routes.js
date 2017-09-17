var express = require('express');
var router = express.Router();

const resourceRouter = require('./resourceRouter');
const pageRouter = require('./pageRouter');

router.use('/resource',resourceRouter);
router.use('/', pageRouter);

module.exports = router;