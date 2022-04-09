module.exports = {
    devServer: {
        port: 3000,
        proxy: {
            '/': {
                target: 'http://localhost:8080',
                ws: true,
                changeOrigin: true
            }
        }
    },
    pages: {
        index: {
            entry: './src/pages/dashboard/main.js',
            template: 'public/index.html',
            title: 'Dashborad',
            chunks: ['chunk-vendors', 'chunk-common', 'index'],
        },
        authentication: {
            entry: './src/pages/authentication/main.js',
            template: 'public/authentication.html',
            title: 'Login',
            chunks: ['chunk-vendors', 'chunk-common', 'authentication']
        }
    }
}