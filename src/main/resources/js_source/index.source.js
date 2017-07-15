var sign_in_app = angular.module('signInSys', ['angular-toArrayFilter', 'ngRoute', 'datetime']);
sign_in_app.controller('navbar_ctrl', ['$scope', function ($scope) {
}]);
sign_in_app.controller('sign_in_info_ctrl', ['$scope', '$http', '$q', 'datetime', '$interval', function ($scope, $http, $q, datetime, $interval) {
    $scope.getLatestDate = function () {
        var defer = $q.defer();
        $http({
            method: 'GET',
            url: "/api/sign_in_info/latest_date"
        }).success(function (data) {
            defer.resolve(data);
        });
        return defer.promise;
    };
    $scope.signIn = function () {
        $scope.signInData = {};
        zkonline.GetVerTemplate();
        var verifyTpl = zkonline.VerifyTemplate;
        if (verifyTpl == "" || verifyTpl == null) {
            return;
        }
        for (var i = 0; i < $scope.fingerPrintData.length; i++) {
            var regTpl = $scope.fingerPrintData[i];
            if (zkonline.MatchFinger(regTpl.fingerprint, verifyTpl)) {
                $scope.signInData.fingerprint = regTpl.fingerprint;
                break;
            }
        }
        if ($scope.signInData.fingerprint == null) {
            alert('Sorry，请重新打开浏览器或擦拭指纹仪再试试');
            return;
        }
        $http({
            method: 'POST',
            url: "/api/sign_in_info/addition",
            data: $scope.signInData
        }).success(function (data) {
            $scope.getSignInInfo($scope.getCurrentDateString());
            if (data.statusFeedBack == 1) {
                alert(data.name + "，签到成功哇！");
            }

            else if (data.statusFeedBack == 2) {
                alert(data.name + "，bye bye！");
            }
        }).error(function () {
            alert('签到失败，请联系DZJ');
        });
    };
    $scope.getFingerPrint = function () {
        $http({
            method: 'GET',
            url: "/api/students/fingerprints"
        }).success(function (data) {
            $scope.fingerPrintData = data;
        });
    };
    $scope.getDutyStudent = function (date) {
        $http({
            method: 'GET',
            url: '/api/students/duty_students/' + date
        }).success(function (data) {
            $scope.dutyStudentData = data;
        });
    };
    $scope.getSignInInfo = function (date) {
        $http({
            method: 'GET',
            url: "/api/sign_in_info/" + date
        }).success(function (data) {
            $scope.signInInfoData = data;
        });
    };
    $scope.getStudent = function () {
        $http({
            method: 'GET',
            url: '/api/students'
        }).success(function (data) {
            $scope.studentData = data;
        });
    };
    $scope.firstLoad = function () {
        $scope.getFingerPrint();
        $scope.getStudent();
        $scope.getLatestDate().then(function (date) {
            var parser = datetime("yyyy-MM-dd");
            parser.parse(date);
            $scope.currentDate = parser.getDate();
            $scope.currentWeekday = $scope.weekday[$scope.currentDate.getDay()];
            $scope.getSignInInfo(date);
            $scope.getDutyStudent(date);
            $interval(function () {
                parser.setDate($scope.currentDate);
                if (date == parser.getText()) {
                    $scope.getSignInInfo(date);
                }
            }, 30000);
        });
    };
    $scope.setBarClass = function (bar) {
        var baseClass = "progress-bar palette";
        if (bar == null) {
            return '';
        }
        if (bar.type == 0) {
            return baseClass + ' palette-bar-now palette-arrive';
        }
        else if (bar.type == 1) {
            return baseClass + ' palette-bar palette-orange';
        }
        else if (bar.type == 2) {
            return baseClass + ' palette-bar palette-peter-river';
        }
        else if (bar.type == 3) {
            return baseClass + ' palette-bar palette-peter-river';
        }
        else if (bar.type == 4) {
            return baseClass + ' palette-bar palette-silver';
        }
        else if (bar.type == 5) {
            return baseClass + ' palette-bar';
        }
        else {
            return '';
        }
    };
    $scope.setBarStyle = function (bar) {
        var parser = datetime("HH:mm");
        parser.parse(bar.startTime);
        var startTime = parser.getDate();
        parser.parse(bar.endTime);
        var endTime = parser.getDate();
        var width = 100 * (endTime.getTime() - startTime.getTime()) / (1000 * 24 * 3600) + '%';
        if (bar.type == 5) {
            return {'width': width, 'opacity': 0};
        }
        return {'width': width};
    };
    $scope.setAbsenceStudent = function (student) {
        $scope.askForAbsenceStudent.studentId = student.studentId;
        $scope.hint = student.name;
    };
    $scope.askForAbsence = function () {
        $scope.askForAbsenceStudent.operDate = $scope.currentDate;
        $http({
            method: 'POST',
            url: "/api/absences/addition",
            data: $scope.askForAbsenceStudent
        }).success(function (data) {
            alert("成功！");
            $scope.askForAbsenceStudent = {};
            $scope.hint = "点击选择学生";
            $scope.getSignInInfo($scope.getCurrentDateString());
            $('#is_make_up_cbx').radiocheck('uncheck');
            $scope.askForAbsenceStudent.isMakeUp = 0;
        }).error(function () {
            alert("失败！请联系DZJ");
        });
    };
    $scope.getCurrentDateString = function () {
        var parser = datetime("yyyy-MM-dd");
        parser.setDate($scope.currentDate);
        return parser.getText();
    };
    $(function () {
        $scope.firstLoad();
        $scope.hint = "点击选择学生";
        $scope.weekday = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
        $('#dateInput').on('change', function () {
            $scope.currentWeekday = $scope.weekday[$scope.currentDate.getDay()];
            var date = $scope.getCurrentDateString();
            $scope.getSignInInfo(date);
            $scope.getDutyStudent(date);
        });
        $scope.askForAbsenceStudent = {};

        $('#is_make_up_cbx').radiocheck('uncheck');
        $scope.askForAbsenceStudent.isMakeUp = 0;
        $('#is_make_up_cbx').on('change.radiocheck', function() {
            $scope.askForAbsenceStudent.isMakeUp = 1 - $scope.askForAbsenceStudent.isMakeUp;
        });
    });
}]);
sign_in_app.controller('rank_list_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.getRankListData = function () {
        $http({
            method: 'GET',
            url: "/api/statistics/sum"
        }).success(function (data) {
            for (var i = 0; i < data.length; i++) {
                data[i].stayLabTimeStr = data[i].stayLabTime.toFixed(2);
                data[i].absenceTime = data[i].absenceTime.toFixed(2);
            }
            $scope.maxStayLabTime = Math.max.apply(Math, data.map(function (item) {
                return item.stayLabTime;
            }));
            $scope.rank_list_data = data;
        });
    };
    $scope.getProgressBarWidth = function (stayLabTime) {
        return {'width': (stayLabTime / ($scope.maxStayLabTime * 1) * 100) + '%'};
    };
    $(function () {
        $scope.getRankListData();
    });
}]);
sign_in_app.controller('week_plan_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.getWeekPlanList = function () {
        $http({
            method: 'GET',
            url: "/api/week_plans"
        }).success(function (data) {
            $scope.week_plan_list = data;
        });
    };
    $scope.setDisplayedWeek = function (week_plan) {
        $http({
            method: 'GET',
            url: "/api/week_plans/" + week_plan.id
        }).success(function (data) {
            $scope.plan_records = data;
            $scope.selected_week_plan = week_plan;
        });
    };
    $scope.selectPlanStudent = function (p) {
        $scope.id_plan = p.id;
        $scope.hint_plan = p.studentName;
        $scope.to_write_plan = p.plan;
    };
    $scope.selectAchievementStudent = function (p) {
        $scope.id_achievement = p.id;
        $scope.hint_achievement = p.studentName;
        $scope.to_write_achievement = p.achievement;
    };
    $scope.selectTutorFeedbackStudent = function (p) {
        $scope.id_tutor_feedback = p.id;
        $scope.hint_tutor_feedback = p.studentName;
        $scope.to_write_tutor_feedback = p.tutorFeedback;
    };
    $scope.submitPlan = function () {
        $http({
            method: 'POST',
            url: "/api/week_plans/",
            data: {"id": $scope.id_plan, "plan": $scope.to_write_plan}
        }).success(function () {
            $scope.to_write_plan = "";
            $scope.hint_plan = "请选择";
            alert("填写成功！");
        });
    };
    $scope.submitAchievement = function () {
        $http({
            method: 'POST',
            url: "/api/week_plans/achievements",
            data: {"id": $scope.id_achievement, "plan": $scope.to_write_achievement}
        }).success(function () {
            $scope.to_write_achievement = "";
            $scope.hint_achievement = "请选择";
            alert("填写成功！");
        });
    };
    $scope.submitTutorFeedback = function () {
        $http({
            method: 'POST',
            url: "/api/week_plans/tutor_feedback",
            data: {"id": $scope.id_tutor_feedback, "plan": $scope.to_write_tutor_feedback}
        }).success(function () {
            $scope.to_write_tutor_feedback = "";
            $scope.hint_tutor_feedback = "请选择";
            alert("填写成功！");
        });
    };
    $(function () {
        $scope.getWeekPlanList();
        $scope.setDisplayedWeek($scope.week_plan_list[0]);
        $scope.to_write_plan = "";
        $scope.to_write_achievement = "";
        $scope.to_write_tutor_feedback = "";
        $scope.hint_plan = "请选择";
        $scope.hint_achievement = "请选择";
        $scope.hint_tutor_feedback = "请选择";
    });
}]);
sign_in_app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {
        controller: 'sign_in_info_ctrl',
        templateUrl: 'sign_in_info'
    }).when('/sign_in_info', {
        controller: 'sign_in_info_ctrl',
        templateUrl: 'sign_in_info'
    }).when('/rank_list', {
        controller: 'rank_list_ctrl',
        templateUrl: 'rank_list'
    }).when('/week_plan', {
        controller: 'week_plan_ctrl',
        templateUrl: 'week_plan'
    });
}]);