var gulp = require('gulp'),
    sass = require('gulp-ruby-sass'),
    autoprefixer = require('gulp-autoprefixer'),
    minifycss = require('gulp-minify-css'),
    jshint = require('gulp-jshint'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    concat = require('gulp-concat'),
    umd = require('gulp-umd');


var outputDir = 'dist';


//	Default task 'gulp': Runs both CSS and JS tasks
gulp.task('default', function () {
    gulp.start('css', 'js_source');
});


//	Watch task 'gulp watch': Starts a watch on CSS and JS tasks
gulp.task('watch', function () {
    gulp.watch('src/**/*.scss', ['css']);
    gulp.watch('src/**/*.js_source', ['js_source']);
});


//	CSS task 'gulp css': Compiles all CSS
gulp.task('css', ['css-concat-all']);

//	1) compile all SCSS to CSS
gulp.task('css-compile', function () {
    return sass('src/**/*.scss', {style: 'expanded'})
        .pipe(autoprefixer(['> 5%', 'last 5 versions']))
        .pipe(minifycss({keepBreaks: true}))
        .pipe(gulp.dest(outputDir));
});

//	2) concatenate core + offCanvas in dist dir
gulp.task('css-concat-core', ['css-compile'], function () {
    return gulp.src([
        outputDir + '/css/jquery.mmenu.oncanvas.css',
        outputDir + '/addons/offcanvas/jquery.mmenu.offcanvas.css',
    ])
        .pipe(concat('jquery.mmenu.css'))
        .pipe(gulp.dest(outputDir + '/css'));
});

//	3) concatenate core + offCanvas + addons in dist dir
gulp.task('css-concat-all', ['css-concat-core'], function () {
    return gulp.src([
        outputDir + '/css/jquery.mmenu.oncanvas.css',
        outputDir + '/addons/offcanvas/jquery.mmenu.offcanvas.css',
        outputDir + '/addons/**/*.css',
        outputDir + '/extensions/**/*.css',
        '!**/jquery.mmenu.iconbar.css',
        '!**/jquery.mmenu.widescreen.css'
    ])
        .pipe(concat('jquery.mmenu.all.css'))
        .pipe(gulp.dest(outputDir + '/css'));
});


//	JS task 'gulp js_source': Runs all JS tasks
//		A bit extensive, but it needs to concatenate certain files in a certain order
//		The dependencies ensure everything is done in the right order
gulp.task('js_source', ['js-umd']);

//	1) copy all except for the navbars add-on into dist dir
gulp.task('js_source-copy', function () {
    return gulp.src([
        'src/*/**/*.js_source',
        '!src/addons/navbars/**/*.js_source'
    ])
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest(outputDir));
});

//	2) concatenate navbars add-on into dist dir
gulp.task('js_source-concat-navbar', ['js-copy'], function () {
    return gulp.src([
        'src/addons/navbars/jquery.mmenu.navbars.js_source',
        'src/addons/navbars/**/*.js_source'
    ])
        .pipe(concat('jquery.mmenu.navbars.min.js_source'))
        .pipe(gulp.dest(outputDir + '/addons/navbars'));
});

//	3) concatenate core + offCanvas + scrollBugFix in dist dir
gulp.task('js_source-concat-core', ['js-concat-navbar'], function () {
    return gulp.src([
        outputDir + '/js_source/jquery.mmenu.oncanvas.min.js_source',
        outputDir + '/addons/offcanvas/jquery.mmenu.offcanvas.min.js_source',
        outputDir + '/addons/scrollbugfix/jquery.mmenu.scrollbugfix.min.js_source',
    ])
        .pipe(concat('jquery.mmenu.min.js_source'))
        .pipe(gulp.dest(outputDir + '/js_source'));
});

//	4) concatenate core + offCanvas + scrollBugFix + addons in dist dir
gulp.task('js_source-concat-all', ['js-concat-core'], function () {
    return gulp.src([
        outputDir + '/js_source/jquery.mmenu.oncanvas.min.js_source',
        outputDir + '/addons/offcanvas/jquery.mmenu.offcanvas.min.js_source',
        outputDir + '/addons/scrollbugfix/jquery.mmenu.scrollbugfix.min.js_source',
        outputDir + '/addons/**/*.js_source'
    ])
        .pipe(concat('jquery.mmenu.all.min.js_source'))
        .pipe(gulp.dest(outputDir + '/js_source'));
});

//	5) minify all in dist dir
gulp.task('js_source-minify', ['js-concat-all'], function () {
    return gulp.src([
        outputDir + '/**/*.min.js_source'
    ])
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('default'))
        .pipe(uglify({preserveComments: 'license'}))
        .pipe(gulp.dest(outputDir));
});

//	6) umd core + offCanvas + scrollBugFix + addons in dist dir
gulp.task('js_source-umd', ['js-minify'], function () {
    return gulp.src([
        outputDir + '/js_source/jquery.mmenu.min.js_source',
        outputDir + '/js_source/jquery.mmenu.all.min.js_source',
    ])
        .pipe(umd({
            dependencies: function () {
                return [{
                    name: 'jquery',
                    global: 'jQuery',
                    param: 'jQuery'
                }];
            },
            exports: function () {
                return true;
            },
            namespace: sanitizeNamespaceForUmd
        }))
        .pipe(rename({suffix: '.umd'}))
        .pipe(gulp.dest(outputDir + '/js_source'));
});
function sanitizeNamespaceForUmd(file) {
    path = file.path.split('\\').join('/').split('/');
    path = path[path.length - 1];
    return path.split('.').join('_');
}

