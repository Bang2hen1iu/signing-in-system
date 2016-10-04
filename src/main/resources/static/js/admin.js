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
        $scope.studentData = null;
        $scope.toAddStudentData = null;
        $scope.toDeleteStudentId = null;
        $scope.getStudent();
    });
}]);
app.controller('coursesCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.getStudent = function () {
        $http({
            method: 'GET',
            url: '/api/common_api/students'
        }).success(function (data) {
            $scope.studentData = data;
        });
    };
    $scope.switchStudentCourse = function (student) {
        $scope.currentStudentId = student.studentId;
        $scope.hint = student.name;
        $scope.getCourses();
    };
    $scope.getCourses = function () {
        $http({
            method: 'GET',
            url: '/api/common_api/courses/'+$scope.currentStudentId
        }).success(function (data) {
            $scope.courseData = data;
        });
    };
    $scope.addCourse = function () {
        if($scope.currentStudentId == null){
            alert("请先选择学生！");
            return;
        }
        $scope.toAddCourseData.studentId = $scope.currentStudentId;
        $scope.toAddCourseData.coursePerWeekJsonList = [
            $scope.coursePerWeekJsonList1, $scope.coursePerWeekJsonList2, $scope.coursePerWeekJsonList3];
        $http({
            method: 'POST',
            url: '/api/admin_api/courses/addition',
            data: $scope.toAddCourseData
        }).success(function () {
            alert("新增课程成功！");
            $scope.toAddCourseData = null;
            $scope.coursePerWeekJsonList1 = null;
            $scope.coursePerWeekJsonList2 = null;
            $scope.coursePerWeekJsonList3 = null;
            $scope.getCourses();
        });
    };
    $scope.copyToModifyCourse = function (course) {
        $scope.toModifyCourseData = course;
        $scope.coursePerWeekJsonList1 = course.coursePerWeekJsonList[0];
        $scope.coursePerWeekJsonList2 = course.coursePerWeekJsonList[1];
        $scope.coursePerWeekJsonList3 = course.coursePerWeekJsonList[2];
    };
    $scope.copyToDeleteCourse = function (course) {
        $scope.toDeleteCourseData = course;
    };
    $scope.modifyCourse = function () {
        $scope.toModifyCourseData.coursePerWeekJsonList = [$scope.coursePerWeekJsonList1, $scope.coursePerWeekJsonList2, $scope.coursePerWeekJsonList3];
        $http({
            method: 'PUT',
            url: '/api/admin_api/courses/update',
            data: $scope.toModifyCourseData
        }).success(function () {
            alert("修改课程成功！");
            $scope.toModifyCourseData = null;
            $scope.coursePerWeekJsonList1 = null;
            $scope.coursePerWeekJsonList2 = null;
            $scope.coursePerWeekJsonList3 = null;
            $scope.getCourses();
        });

    };
    $scope.delCourse = function () {
        $http({
            method: 'DELETE',
            url: '/api/admin_api/courses/deletion?studentId='+$scope.toDeleteCourseData.studentId+'&courseName=' + $scope.toDeleteCourseData.courseName
        }).success(function () {
            alert("删除课程成功！");
            $scope.toDeleteCourseData = null;
            $scope.getCourses();
        });
    };
    $(function () {
        $scope.hint = '请选择学生';
        $scope.studentData = null;
        $scope.currentStudentId = null;
        $scope.courseData = null;
        $scope.toAddCourseData = null;
        $scope.toModifyCourseData = null;
        $scope.toDeleteCourseData = null;
        $scope.coursePerWeekJsonList1 = null;
        $scope.coursePerWeekJsonList2 = null;
        $scope.coursePerWeekJsonList3 = null;
        $scope.getStudent();
    });
}]);
app.controller('dutyStudentsCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.getStudent = function () {
        $http({
            method: 'GET',
            url: '/api/common_api/students'
        }).success(function (data) {
            $scope.studentData = data;
        });
    };
    $scope.getDutyStudent = function () {
        $http({
            method: 'GET',
            url: '/api/admin_api/duty_students'
        }).success(function (data) {
            $scope.dutyStudentData = data;
        });
    };
    $scope.switchStudentCourse = function (student) {
        $scope.toAddDutyData.studentId = student.studentId;
        $scope.hint = student.name;
    };
    $scope.addDutyStudent = function () {
        $http({
            method: 'POST',
            url: '/api/admin_api/duty_student/addition',
            data: $scope.toAddDutyData
        }).success(function (data) {
            alert("添加成功！");
            $scope.toAddDutyData.studentId = null;
            $scope.hint = '请选择值日生';
            $scope.getDutyStudent();
        });
    };
    $scope.delDutyStudent = function (id) {
        $http({
            method: 'DELETE',
            url: '/api/admin_api/duty_student/deletion?studentId='+id
        }).success(function (data) {
            alert("删除成功！");
            $scope.getDutyStudent();
        });
    };
    $(function () {
        $scope.hint = '请选择值日生';
        $scope.studentData = null;
        $scope.toAddDutyData = {};
        $scope.toAddDutyData.operDate = new Date();
        $scope.getStudent();
        $scope.getDutyStudent();
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