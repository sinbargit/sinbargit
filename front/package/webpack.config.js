let webpack = require('webpack');

module.exports = {
    devtool: 'eval-source-map',
    entry:  "./app/src/index.js",
    output: {
        path: __dirname + "/build",
        filename: "bundle.js"
    },
    module: {
        rules: [
            { test: /\.json$/, use: {loader:"json-loader"} },
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    query: {
                        presets: ['latest','react']
                    }
                }
            },
            { test: /\.css$/, use: [ 'style-loader',
                {
                    loader: 'css-loader',
                    options: {
                        importLoaders: 1
                    }
                }] }
        ]
    },
    plugins:[
        new webpack.optimize.UglifyJsPlugin(
            {
                uglifyOptions: {
                    warnings: false,
                    output: {
                        comments: false,
                        beautify: false
                    }
                }
            }
        ),
        new webpack.DefinePlugin({
            'process.env': {
                // 'NODE_ENV': JSON.stringify('production')
                'NODE_ENV': JSON.stringify('development')
            }
        })
    ]
}