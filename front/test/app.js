const express = require('express');
const app = express();

const routes = require('./routes');

app.use('/', routes);

app.listen(3000, function () {
    console.log('Example app listening on port 3000!')
});