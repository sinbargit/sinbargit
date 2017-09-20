let webpack = require('webpack');

module.exports = {
    module: {
        rules: [
            {test: /\.json$/, use: {loader: "json-loader"}},
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    query: {
                        presets: ['latest', 'react']
                    }
                }
            },
            {
                test: /\.css$/, use: ['style-loader',
                {
                    loader: 'css-loader',
                    options: {
                        importLoaders: 1
                    }
                }]
            }
        ]
    }
}