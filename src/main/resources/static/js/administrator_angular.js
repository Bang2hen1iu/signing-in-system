var css_app = angular.module('css', ['ngRoute']);
css_app.controller('navbar_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.logout = function () {
        $http({
            method: 'DELETE',
            url: "/logout"
        }).success(function () {
            window.location = '/login';
        });
    };
    $scope.getUserProfile = function () {
        $http({
            method: 'GET',
            url: "/administrator/administrator_info"
        }).success(function (data) {
            $scope.user_profile_data = data;
        });
    };
    $scope.modifyPassword = function () {
        $http({
            method: 'PUT',
            url: "/administrator/" + $scope.new_password + "/password_modification/"
        }).success(function (data) {
            $scope.new_password = '';
        });
    };
    $(function () {
        $scope.user_profile_data = {};
        $scope.new_password = '';
    });
}]);
css_app.controller('manageStudentCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.addStudent = function () {
        $http({
            method: 'POST',
            url: "/administrator/student/addition",
            data: $scope.student_creation_data
        }).success(function () {
            $scope.student_creation_data = {};
            $scope.searchStudent();
        });
    };
    $scope.searchStudent = function () {
        if ($scope.search_key == '学生编号') {
            $scope.student_search_data.name = undefined;
        }
        else if ($scope.search_key == '学生姓名') {
            $scope.student_search_data.studentId = undefined;
        }
        $http({
            method: 'POST',
            url: "/administrator/student/search",
            data: $scope.student_search_data
        }).success(function (data) {
            $scope.student_search_data = {};
            $scope.student_data = data;
        });
    };
    $scope.copyStudent = function (student) {
        angular.copy(student, $scope.student_modification_data);
    };
    $scope.modifyStudent = function () {
        $http({
            method: 'POST',
            url: "/administrator/student/modification",
            data: $scope.student_modification_data
        }).success(function () {
            $scope.student_modification_data = {};
        });
    };
    $scope.retrieveCourseSelection = function (student_id) {
        $scope.course_selection_search_data.studentId = student_id;
        $http({
            method: 'POST',
            url: "/administrator/course_selection/search",
            data: $scope.course_selection_search_data
        }).success(function (data) {
            $scope.course_selection_search_data = {};
            $scope.course_selection_data = data;
        });
    };
    $scope.delStudent = function () {
        $http({
            method: 'DELETE',
            url: "/administrator/" + $scope.toDeleteStudentId + "/student/deletion"
        }).success(function (data) {
            $scope.toDeleteStudentId = {};
            $scope.searchStudent();
        });
    };
    $scope.assignToDeleteStudentId = function (student_id) {
        $scope.toDeleteStudentId = student_id;
    };
    $(function () {
        $scope.student_search_data = {};
        $scope.student_creation_data = {};
        $scope.course_selection_search_data = {};
        $scope.student_modification_data = {};
        $scope.toDeleteStudentId = {};
        //$scope.searchStudent();
    });
}]);
css_app.controller('manageCourseCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.addCourse = function () {
        $scope.course_creation_data.teacherId = $scope.selected_teacher.teacherId;
        console.log($scope.course_creation_data);
        $http({
            method: 'POST',
            url: "/administrator/course/addition",
            data: $scope.course_creation_data
        }).success(function () {
            $scope.searchCourse();
            $scope.selected_teacher = {};
            $scope.course_creation_data = {};
        });
    };
    $scope.searchCourse = function () {
        if ($scope.search_key == '课程编号') {
            $scope.course_search_data.courseName = undefined;
        }
        else if ($scope.search_key == '课程名称') {
            $scope.course_search_data.courseId = undefined;
        }
        $http({
            method: 'POST',
            url: "/administrator/course/search",
            data: $scope.course_search_data
        }).success(function (data) {
            if (data.length > 0) {
                $scope.course_data = data;
                $scope.course_search_data = {};
            }
            else {
                $scope.course_data = {};
            }
        });
    };
    $scope.retrieveCourseSelection = function (course_id) {
        $scope.course_selection_search_data.courseId = course_id;
        $http({
            method: 'POST',
            url: "/administrator/course_selection/search",
            data: $scope.course_selection_search_data
        }).success(function (data) {
            $scope.course_selection_search_data = {};
            $scope.course_selection_data = data;
        });
    };
    $scope.getTeacherList = function () {
        if ($scope.teacher_list.length > 0) {
            return;
        }
        $http({
            method: 'POST',
            url: "/administrator/teacher/search",
            data: {}
        }).success(function (data) {
            $scope.teacher_list = data;
        });
    };
    $scope.copyCourse = function (course) {
        angular.copy(course, $scope.course_modification_data);
    };
    $scope.modifyCourse = function () {
        $http({
            method: 'POST',
            url: "/administrator/course/modification",
            data: $scope.course_modification_data
        }).success(function () {
            $scope.course_modification_data = {};
        });
    };
    $scope.assignToDeleteCourseId = function (course_id) {
        $scope.toDeleteCourseId = course_id;
    };
    $scope.delCourse = function () {
        $http({
            method: 'DELETE',
            url: "/administrator/" + $scope.toDeleteCourseId + "/course/deletion"
        }).success(function (data) {
            $scope.toDeleteCourseId = {};
            $scope.searchCourse();
        });
    };

    $(function () {
        $scope.course_creation_data = {};
        $scope.course_search_data = {};
        $scope.course_selection_search_data = {};
        $scope.course_modification_data = {};
        $scope.toDeleteCourseId = {};
        $scope.teacher_list = {};
        $scope.selected_teacher = {};
        //$scope.searchCourse();
    });
}]);
css_app.controller('manageTeacherCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.addTeacher = function () {
        $http({
            method: 'POST',
            url: "/administrator/teacher/addition",
            data: $scope.teacher_creation_data
        }).success(function () {
            $scope.teacher_creation_data.name = '';
            $scope.teacher_creation_data.feasibleTeachingCourse = new Array();
            $scope.searchTeacher();
        });
    };
    $scope.searchTeacher = function () {
        if ($scope.search_key == '教师编号') {
            $scope.teacher_search_data.name = undefined;
        }
        else if ($scope.search_key == '教师姓名') {
            $scope.teacher_search_data.teacherId = undefined;
        }
        $http({
            method: 'POST',
            url: "/administrator/teacher/search",
            data: $scope.teacher_search_data
        }).success(function (data) {
            $scope.teacher_search_data = {};
            $scope.teacher_data = data;
        });
    };
    $scope.retrieveCourse = function (teacher_id) {
        $scope.course_search_data.teacherId = teacher_id;
        $http({
            method: 'POST',
            url: "/administrator/course/search",
            data: $scope.course_search_data
        }).success(function (data) {
            $scope.course_search_data = {};
            $scope.course_data = data;
        });
    };
    $scope.copyTeacher = function (teacher) {
        angular.copy(teacher, $scope.teacher_modification_data);
    };
    $scope.modifyTeacher = function () {
        $http({
            method: 'POST',
            url: "/administrator/teacher/modification",
            data: $scope.teacher_modification_data
        }).success(function () {
            $scope.searchTeacher();
            $scope.teacher_modification_data = {};
        });
    };
    $scope.delTeacher = function () {
        $http({
            method: 'DELETE',
            url: "/administrator/" + $scope.toDeleteTeacherId + "/teacher/deletion"
        }).success(function (data) {
            $scope.toDeleteTeacherId = {};
            $scope.searchTeacher();
        });
    };
    $scope.assignToDeleteTeacherId = function (teacher_id) {
        $scope.toDeleteTeacherId = teacher_id;
    };

    $(function () {
        $scope.teacher_creation_data = {};
        $scope.teacher_search_data = {};
        $scope.course_search_data = {};
        $scope.teacher_modification_data = {};
        $scope.toDeleteStudentId = {};
        $scope.teacher_creation_data.feasibleTeachingCourse = new Array();
        //$scope.searchTeacher();
    });
}]);
css_app.controller('manageCourseSelectionCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.addCourseSelection = function () {
        $http({
            method: 'POST',
            url: "/administrator/course_selection/addition",
            data: $scope.course_selection_creation_data
        }).success(function () {
            $scope.searchCourseSelection();
            $scope.course_selection_creation_data = {};
        });
    };
    $scope.searchCourseSelection = function () {
        if ($scope.student_search_key == '学生编号') {
            $scope.course_selection_search_data.name = undefined;
        }
        else if ($scope.student_search_key == '学生姓名') {
            $scope.course_selection_search_data.studentId = undefined;
        }
        if ($scope.course_search_key == '课程编号') {
            $scope.course_selection_search_data.courseName = undefined;
        }
        else if ($scope.course_search_key == '课程名称') {
            $scope.course_selection_search_data.courseId = undefined;
        }
        $http({
            method: 'POST',
            url: "/administrator/course_selection/search",
            data: $scope.course_selection_search_data
        }).success(function (data) {
            if (data.length > 0) {
                $scope.course_selection_data = data;
                $scope.course_selection_search_data = {};
                $scope.calAverageGrades();
            }
            else {
                $scope.course_selection_data = {};
            }
        });
    };
    $scope.calAverageGrades = function () {
        var sum = 0;
        var data = $scope.course_selection_data;
        for (var i = 0; i < data.length; i++) {
            sum += parseInt($scope.course_selection_data[i].grade);
        }
        $scope.average_grades = (sum / $scope.course_selection_data.length).toFixed(2);
    };
    $scope.copyCourseSelection = function (course_selection) {
        angular.copy(course_selection, $scope.course_selection_modification_data);
    };
    $scope.modifyCourseSelection = function () {
        $http({
            method: 'POST',
            url: "/administrator/course_selection/modification",
            data: $scope.course_selection_modification_data
        }).success(function () {
            $scope.searchCourseSelection();
            $scope.course_selection_modification_data = {};
        });
    };
    $scope.assignToDeleteCourseSelection = function (course_selection) {
        angular.copy(course_selection, $scope.toDeleteCourseSelection);
    };
    $scope.delCourseSelection = function () {
        $http({
            method: 'DELETE',
            url: "/administrator/" + $scope.toDeleteCourseSelection.studentId + "_" + $scope.toDeleteCourseSelection.courseId + "/course_selection/deletion"
        }).success(function (data) {
            $scope.searchCourseSelection();
            $scope.toDeleteCourseSelection = {};
        });
    };

    $(function () {
        $scope.course_selection_creation_data = {};
        $scope.course_selection_search_data = {};
        $scope.course_selection_modification_data = {};
        $scope.toDeleteCourseSelection = {};
        //$scope.searchCourseSelection();
    });
}]);
css_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'administrator_welcome'
    }).when('/student/management', {
        controller: 'manageStudentCtrl',
        templateUrl: 'student_management'
    }).when('/course/management', {
        controller: 'manageCourseCtrl',
        templateUrl: 'course_management'
    }).when('/teacher/addition', {
        controller: 'addTeacherCtrl',
        templateUrl: 'teacher_creation'
    }).when('/teacher/management', {
        controller: 'manageTeacherCtrl',
        templateUrl: 'teacher_management'
    }).when('/course_selection/management', {
        controller: 'manageCourseSelectionCtrl',
        templateUrl: 'course_selection_management'
    });
}]);