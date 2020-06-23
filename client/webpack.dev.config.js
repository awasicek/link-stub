const webpack = require("webpack");
const merge = require("webpack-merge");
const baseConfig = require("./webpack.base.config.js");

module.exports = merge(baseConfig, {
    plugins: [
        new webpack.DefinePlugin({
            "process.env.HOST": JSON.stringify("http://localhost:5000")
        })
    ]
});