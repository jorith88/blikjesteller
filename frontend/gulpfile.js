var source = "./src";
var target = "./target";

var gulp = require('gulp');
var changed = require('gulp-changed');
var concat = require('gulp-concat');
var stripDebug = require('gulp-strip-debug');
var uglify = require('gulp-uglify');
var watch = require('gulp-watch');
var sourcemaps = require('gulp-sourcemaps');
var sass = require('gulp-sass');

var js_src = [
    'node_modules/angular/angular.min.js',
    'node_modules/bootstrap/js/bootstrap.min.js',
    'node_modules/fastclick/lib/fastclick.js',
    source + '/js/angular.blikje.js'
]

var assets_src = [
    source + '/assets/**/*',
    source + '/*.*',
];

var sass_src = [
    source + '/sass/**/*.scss',
    'node_modules/bootstrap/dist/css/bootstrap.min.css'
];

// JS concat, strip debugging and minify
gulp.task('scripts', function() {
    return gulp.src(js_src)
        .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(concat('script.js'))
        .pipe(stripDebug())
        .pipe(uglify({ output: { ascii_only: true } }))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(target + '/global/js/'));
});

gulp.task('assets', function() {
    gulp.src(assets_src,{base:source + '/assets/'})
        .pipe(gulp.dest(target + '/global/'));
});

gulp.task('sass', function () {
    return gulp.src(sass_src)
        .pipe(sourcemaps.init())
        .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(target + '/global/css/'));
});

gulp.task('default',['sass','scripts', 'assets'], function(){
    //gulp.watch(sass_src, ['sass']);
    //gulp.watch(js_base_src, ['scripts']);
    //gulp.watch(js_ie9_src, ['scripts_ie9']);
    //gulp.watch(assets_src, ['assets']);
});