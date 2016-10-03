var app = angular.module('signInSys', ['ngRoute']);
app.controller('signInSysCtrl', ['$scope', '$http', function ($scope,$http) {
    $scope.testToken = function () {
        $http({
            method: 'GET',
            url: '/api/admin_api/admin/token_validation'
        }).success(function () {
            console.log("用户token验证通过");
        }).error(function () {
            window.location = '/admin/login';
        })
    };
    $(function () {
        $scope.testToken();
    });
}]);
app.controller('navbar_ctrl', ['$scope', '$http', function ($scope, $http) {

    $(function () {

    });
}]);
app.controller('studentsCtrl', ['$scope', '$http', function ($scope, $http) {

    $(function () {

    });
}]);
app.controller('coursesCtrl', ['$scope', '$http', function ($scope, $http) {

    $(function () {

    });
}]);
app.controller('dutyStudentsCtrl', ['$scope', '$http', function ($scope, $http) {

    $(function () {

    });
}]);
app.controller('statisticsCtrl', ['$scope', '$http', function ($scope, $http) {

    $(function () {

    });
}]);
app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'admin_welcome'
    }).when('/students', {
        controller: 'studentsCtrl',
        templateUrl: 'students'
    }).when('/courses', {
        controller: 'coursesCtrl',
        templateUrl: 'courses'
    }).when('/duty_students', {
        controller: 'dutyStudentsCtrl',
        templateUrl: 'duty_students'
    }).when('/statistics', {
        controller: 'statisticsCtrl',
        templateUrl: 'statistics'
    });
}]);