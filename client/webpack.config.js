const HTMLWebPackPlugin = require("html-webpack-plugin");
const path = require("path");

module.exports = {
    module: {
        rules: [
            /* rules for babel-loader (transform js dependencies with babel - e.g.,
               import components into other components) */
            {
                test: /\.jsx?$/, // js and jsx
                exclude: /node_modules/,
                use: "babel-loader"
            }
        ]
    },
    plugins: [
        /* generate HTML file with <script> injected, write to dist/index.html
           and minify */
        new HTMLWebPackPlugin({
            template: "./src/index.html",
            filename: "./index.html"
        })
    ],
    resolve: {
        extensions: [".js", ".jsx"],
        alias: {
            "@components": path.resolve(__dirname, "src/components/"),
            "@services": path.resolve(__dirname, "src/services/"),
            "@utils": path.resolve(__dirname, "src/utils/")
        }
    },
    entry: {
        index: "./src/index.js"
    },
    output: {
        filename: "main.js",
        path: path.resolve(__dirname, "dist")
    },
    // de-dupe dependencies
    optimization: {
        splitChunks: {
            chunks: "all"
        }
    }
};