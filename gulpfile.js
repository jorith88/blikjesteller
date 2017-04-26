var source = "./_dev";
var target = "./web";

var gulp = require('gulp');
var changed = require('gulp-changed');
var concat = require('gulp-concat');
var stripDebug = require('gulp-strip-debug');
var uglify = require('gulp-uglify');
var watch = require('gulp-watch');
var sourcemaps = require('gulp-sourcemaps');
var sass = require('gulp-sass');
var del = require('del');
var connect = require('gulp-connect');

var js_src = [
    'node_modules/jquery/dist/jquery.min.js',
    'node_modules/vue/dist/vue.common.min.js',
    'node_modules/bootstrap/js/bootstrap.min.js',
    'node_modules/fastclick/lib/fastclick.js',
    source + '/js/utility.js',
    source + '/js/blikje.js'
]

var assets_src = [
    source + '/assets/**/*',
    source + '/*.*',
];

var sass_src = [
    source + '/sass/**/*.scss'
];

// JS concat, strip debugging and minify
gulp.task('scripts', function() {
    return gulp.src(js_src)
        .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(concat('script.js'))
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

gulp.task('watch',['clean', 'sass','scripts', 'assets'], function(){
    gulp.watch(sass_src, ['sass']);
    gulp.watch(js_src, ['scripts']);
    gulp.watch(assets_src, ['assets']);
});

gulp.task('clean', function() {
    del.sync(target + '/index.html');
    del.sync(target + '/global');
});

gulp.task('webserver', ['watch'],  function() {
  connect.server({
    root: 'web',
    livereload: true,
  });
});

gulp.task('default',['clean','sass','scripts', 'assets'], function(){});