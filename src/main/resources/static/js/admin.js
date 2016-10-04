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
    $scope.addStudent = function () {
        $http({
            method: 'POST',
            url: '/api/admin_api/students/addition',
            data: $scope.toAddStudentData
        }).success(function (data) {
            alert("新增学生成功！");
            $scope.toAddStudentData = null;
            $scope.getStudent();
        });
    };
    $scope.getStudent = function () {
        $http({
            method: 'GET',
            url: '/api/common_api/students'
        }).success(function (data) {
            $scope.studentData = data;
        });
    };
    $scope.copyStudentId = function (id) {
        $scope.toDeleteStudentId = id;
    };
    $scope.delStudent = function () {
        $http({
            method: 'DELETE',
            url: '/api/admin_api/student/deletion/'+$scope.toDeleteStudentId
        }).success(function () {
            alert("删除成功！");
            $scope.toDeleteStudentId = null;
            $scope.getStudent();
        });
    };
    $(function () {
        $scope.studentData = {};
        $scope.toAddStudentData = null;
        $scope.toDeleteStudentId = null;
        $scope.getStudent();
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