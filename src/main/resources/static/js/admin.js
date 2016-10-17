var app = angular.module('signInSys', ['ngRoute', 'datetime', 'angular-toArrayFilter']);
app.controller('signInSysCtrl', ['$scope', '$http', function ($scope, $http) {
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
    $scope.logout = function () {
        $http({
            method: 'DELETE',
            url: '/api/admin_api/admin/logout'
        }).success(function () {
            window.location = '/admin/login';
        });
    };
    $scope.modifyAdminPassword = function () {
        $http({
            method: 'PUT',
            url: '/api/admin_api/admin/password_update/'+$scope.adminPassword
        }).success(function () {
            alert("修改成功！");
        }).error(function () {
            alert("修改失败！");
        })
    };
    $scope.modifyLabPassword = function () {
        $http({
            method: 'PUT',
            url: '/api/sign_in_info_api/admin/password_update/'+$scope.labPassword
        }).success(function () {
            alert("修改成功！");
        }).error(function () {
            alert("修改失败！");
        })
    };

    $(function () {
        $scope.adminPassword = "";
        $scope.labPassword = "";
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
    $scope.registerStudent = function (student) {
        $scope.toRegisterStudent = student;
        alert('aa');
        zkonline.Register();
        alert('bb');
        $scope.toRegisterStudent.fingerprint = zkonline.RegisterTemplate;
        $http({
            method: 'POST',
            url: '/api/admin_api/students/registering',
            data:$scope.toRegisterStudent
        }).success(function () {
            alert("成功！");
        });
    };
    $scope.copyToDeleteStudent = function (student) {
        $scope.toDeleteStudent = student;
    };
    $scope.delStudent = function () {
        $http({
            method: 'DELETE',
            url: '/api/admin_api/student/deletion/'+$scope.toDeleteStudent.studentId
        }).success(function () {
            alert("删除成功！");
            $scope.toDeleteStudent = null;
            $scope.getStudent();
        });
    };
    $(function () {
        $scope.studentData = null;
        $scope.toAddStudentData = null;
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
app.controller('statisticsCtrl', ['$scope', '$http', 'datetime', function ($scope, $http, datetime) {
    $scope.queryStatistic = function () {
        var parser = datetime("yyyy-MM-dd");
        parser.setDate($scope.startDate);
        $scope.toQueryData.startDate = parser.getText();
        parser.setDate($scope.endDate);
        $scope.toQueryData.endDate = parser.getText();
        $http({
            method: 'GET',
            url: '/api/admin_api/statistics/range_query?startDate='+$scope.toQueryData.startDate+'&endDate='+$scope.toQueryData.endDate
        }).success(function (data) {
            $scope.maxStayLabTime = Math.max.apply(Math,data.map(function(item){return item.stayLabTime;}));
            $scope.statistics = data;
            $scope.toQueryData = {};
        });
    };
    $scope.getProgressBarWidth = function(stayLabTime){
        return {'width':(stayLabTime/($scope.maxStayLabTime*1.3)*100)+'%'};
    };
    $(function () {
        $scope.statistics = {};
        $scope.toQueryData = {};
        $scope.maxStayLabTime = null;
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