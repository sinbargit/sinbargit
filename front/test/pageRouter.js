var express = require('express');
var router = express.Router();

router.get('/resource', function(req, res, next) {
    console.log('hit /resource in pageRouter');
    res.send('respond with page by /resource');
});

router.get('/new-page', function(req, res, next) {
    res.send('respond with page by /new-page');
});

router.get('*', function(req, res, next) {
    console.log('hit * in pageRouter');
    res.send('respond with page by *');
});

module.exports = router;