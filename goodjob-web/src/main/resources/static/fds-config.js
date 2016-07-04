module.exports = {
    basePath: __dirname,
    publicFolder: 'app',
    viewFolder: 'app',
    mockFolder: 'mock',
    routeFile: 'routes.js',
    mockExts: ['.js', '.json'],
    proxy: null,
    port: 3000,
    enableJava: true,
    javaServerPort: 12321,
    livereload: false,
    open: {
        route: '/task',
        browser: ['google chrome']
    }
};
