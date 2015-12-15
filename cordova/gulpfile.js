var APP_VERSION = require('./package.json').version;

var helpText = 'All tasks (except \'clean\') need a --env option to define the environment this app will be built for. The appConfig.js in the root\n'
              + 'of the web folder will be overwritten by the content in resources/configuration/appConfig-{environment}.js. The sytem will fall back to dev\n'
              + 'if no environment is specified.\n'
              + '\n'
              + 'The available environments are:\n'
              + '  - dev\n'
              + '  - uat\n'
              + '  - prod\n'
              + '\n'
              + 'Example: gulp build --env=prod\n';

var gulp = require('gulp-help')(require('gulp'), {
    afterPrintCallback: function() {
        console.log(helpText);
    }
});

var cordova = require('cordova-lib').cordova;
var CordovaConfig = require('cordova-config');
var del = require('del');
var environments = require('gulp-environments');
var rename = require('gulp-rename');
var cheerio = require('gulp-cheerio');
var fs = require('fs');
var extend = require('extend');

// Define platforms
var platforms = ['android'];

// Only build the iOS app on OS X
if (process.platform === 'darwin') {
    platforms.push('ios');
}

// Define plugins
var plugins = ['cordova-plugin-whitelist','cordova-plugin-camera','cordova-plugin-statusbar','cordova-plugin-inappbrowser'];

// Set up environments
var dev = environments.make('dev');
var uat = environments.make('uat');
var prod = environments.make('prod');
var isEnvironmentSet = dev() || uat() || prod();

if (!isEnvironmentSet) {
    console.log('\nWARNING: no environment has been set (--env dev/uat/prod).\n');
    environments.current(dev);
}

console.log('Building DealerApp v' + APP_VERSION + ' for environment: ' + environments.current().$name + '\n');

var appDir = './target';
var webappDir = '../frontend/target';
var configFile = 'resources/configuration/appConfig-' + environments.current().$name + '.json';

/**
 * This function does a couple of things:
 *   - copy cordovaConfig.xml to target/config.xml
 *   - copy index.htm from the web project to the target folder while injecting a cordova.js script tag
 *   - copy appConfig-{environment}.json to target/appConfig.json
 *   - copy all other frontend files from the web project to target/
 **/
var buildFrontend = function(callback) {
    console.log('- Create config.xml...');
    gulp.src('cordovaConfig.xml')
        .pipe(rename('config.xml'))
        .pipe(gulp.dest(appDir))
        .on('end', function() {
            var config = new CordovaConfig(appDir + '/config.xml');
            config.setVersion(APP_VERSION);
            config.writeSync();
        });

    console.log('- Create index.htm...');
    gulp.src(webappDir + '/index.htm')
        .pipe(cheerio(function($, file) {
            $('<script src="cordova.js"></script>\n').insertBefore($('script').first());
        }))
        .pipe(gulp.dest(appDir + '/www'));

    console.log('- Copy frontend files...');
    gulp.src([webappDir + '/global/**',])
        .pipe(gulp.dest(appDir + '/www/global/'))
        .on('end', function() {
            callback();
        });
};

/****** Gulp tasks below ******/

gulp.task('create', 'Create a Cordova project structure including platforms, plugins, and frontend. The iOS app will only be created on an OS X system.',['clean'], function (callback) {
    buildFrontend(function() {
        process.chdir(appDir);

        console.log('- Create appConfig.json');
        var defaultConfig = require(webappDir + '/appConfig.json'),
            environmentOverrides = require('./resources/configuration/appConfig-' + environments.current().$name + '.json'),
            environmentConfig = extend(defaultConfig, environmentOverrides, {appVersion: APP_VERSION});

        fs.writeFileSync('www/appConfig.json', JSON.stringify(environmentConfig));

        console.log('- Add platforms: ' + platforms);
        return cordova.platform('add', platforms, function() {

          console.log('- Add plugins: ' + plugins);
          cordova.plugin('add', plugins, callback);
        });
    });
});

gulp.task('build', 'Build the app.', ['create'], function (callback) {
    console.log('- Building app...');

    var options = prod() ? ['--release'] : [];

    return cordova.build({
        'platforms': platforms,
        'options': options
    }, callback);
});

gulp.task('clean', 'Remove the generated target directory.', function() {
    return del(appDir);
});

gulp.task('run', 'Run the app on an Android phone (iOS doesn\'t support this).', ['create'], function() {
    return cordova.run('android');
});