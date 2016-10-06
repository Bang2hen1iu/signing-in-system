var sign_in_app = angular.module('signInSys', ['angular-toArrayFilter', 'ngRoute', 'datetime']);
sign_in_app.controller('navbar_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.login = function () {
        console.log("wa");
        $http({
            method: 'POST',
            url: "/api/sign_in_info_api/admin/validation",
            data: $scope.login_data
        }).success(function () {
            alert('登录成功');
        }).error(function () {
            alert('登录失败');
        });
    };
    $scope.logout = function () {
        $http({
            method: 'DELETE',
            url: "/api/sign_in_info_api/admin/logout"
        }).success(function (data) {
            alert('已成功退出登录');
        });
    };
    $(function () {
        $scope.login_data = {};
    });
}]);
sign_in_app.controller('sign_in_info_ctrl', ['$scope', '$http', '$q', 'datetime', '$interval', function ($scope, $http, $q, datetime, $interval) {
    $scope.getLatestDate = function () {
        var defer = $q.defer();
        $http({
            method: 'GET',
            url: "/api/common_api/sign_in_info/latest_date"
        }).success(function (data) {
            defer.resolve(data);
        });
        return defer.promise;
    };
    $scope.getDutyStudent = function (date) {
        $http({
            method: 'GET',
            url: '/api/common_api/duty_students/'+date
        }).success(function (data) {
            $scope.dutyStudentData = data;
        });
    };
    $scope.getSignInInfo = function (date) {
        $http({
            method: 'GET',
            url: "/api/common_api/sign_in_info/"+date
        }).success(function (data) {
            $scope.signInInfoData = data;
        });
    };
    $scope.firstLoad = function () {
        $scope.getLatestDate().then(function (date) {
            var parser = datetime("yyyy-MM-dd");
            parser.parse(date);
            $scope.currentDate = parser.getDate();
            $scope.getSignInInfo(date);
            $scope.getDutyStudent(date);
            $interval(function () {
                parser.setDate($scope.currentDate);
                if(date==parser.getText()){
                    $scope.getSignInInfo(date);
                }
            }, 30000);
        });
    };
    $scope.checkOnTime = function (order, tsp) {
        var parser = datetime("yyyy-MM-dd HH:mm:ss");
        var parser_date = datetime("yyyy-MM-dd");
        parser_date.setDate($scope.currentDate);
        var date = parser_date.getText();
        switch (order){
            case 1:
                parser.parse(date + ' 09:00:00');
                return parser.getDate() > new Date(tsp);
            case 2:
                parser.parse(date + ' 11:30:00');
                return parser.getDate() < new Date(tsp);
            case 3:
                parser.parse(date + ' 14:00:00');
                return parser.getDate() > new Date(tsp);
            case 4:
                parser.parse(date + ' 17:00:00');
                return parser.getDate() < new Date(tsp);
            case 5:
                parser.parse(date + ' 18:30:00');
                return parser.getDate() > new Date(tsp);
            case 6:
                parser.parse(date + ' 21:30:00');
                return parser.getDate() < new Date(tsp);
            default:
                return false;
        }
    };
    $scope.setTrStyle = function (order, tsp) {
        if(tsp==null){
            return '';
        }
        if($scope.checkOnTime(order, tsp)){
            return 'warning';
        }
        else{
            return 'info';
        }
    };
    $scope.copySignatureImgName = function (signatureImgName) {
        $scope.signatureImgPath = '/api/common_api/sign_in_info/signatures/'+signatureImgName;
    };
    $(function () {
        $scope.signInInfoData = {};
        $scope.dutyStudentData = {};
        $scope.signatureImgPath = null;
        $scope.firstLoad();
        $('#dateInput').on('change',function () {
            var parser = datetime("yyyy-MM-dd");
            parser.setDate($scope.currentDate);
            var date = parser.getText();
            $scope.getSignInInfo(date);
            $scope.getDutyStudent(date);
        });
    });
}]);
sign_in_app.controller('rank_list_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.getRankListData = function () {
        $http({
            method: 'GET',
            url: "/api/common_api/statistics/sum"
        }).success(function (data) {
            $scope.maxStayLabTime = Math.max.apply(Math,data.map(function(item){return item.stayLabTime;}));
            $scope.rank_list_data = data;
        });
    };
    $scope.getProgressBarWidth = function (stayLabTime) {
        return {'width':(stayLabTime/($scope.maxStayLabTime*1.3)*100)+'%'};
    };
    $(function () {
        $scope.rank_list_data = {};
        $scope.maxStayLabTime = null;
        $scope.getRankListData();
    });
}]);
sign_in_app.controller('sign_in_action_ctrl', ['$scope', '$http', '$q', 'datetime', function ($scope, $http, $q, datetime) {

    var mousePressed = false;
    var lastX, lastY;
    var ctx;

    var InitThis = function() {
        ctx = document.getElementById('myCanvas').getContext("2d");

        $('#myCanvas').mousedown(function (e) {
            mousePressed = true;
            Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, false);
        });

        $('#myCanvas').mousemove(function (e) {
            if (mousePressed) {
                Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, true);
            }
        });

        $('#myCanvas').mouseup(function (e) {
            mousePressed = false;
        });
        $('#myCanvas').mouseleave(function (e) {
            mousePressed = false;
        });
    };

    var Draw = function(x, y, isDown) {
        if (isDown) {
            ctx.beginPath();
            ctx.strokeStyle = $('#selColor').val();
            ctx.lineWidth = $('#selWidth').val();
            ctx.lineJoin = "round";
            ctx.moveTo(lastX, lastY);
            ctx.lineTo(x, y);
            ctx.closePath();
            ctx.stroke();
        }
        lastX = x; lastY = y;
    };

    var clearArea = function() {
        // Use the identity matrix while clearing the canvas
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    };

    $scope.getLatestDate = function () {
        var defer = $q.defer();
        $http({
            method: 'GET',
            url: "/api/common_api/sign_in_info/latest_date"
        }).success(function (data) {
            $scope.currentDate = data;
            defer.resolve(data);
        });
        return defer.promise;
    };
    $scope.getSignInInfo = function (date) {
        $http({
            method: 'GET',
            url: "/api/common_api/sign_in_info/"+date
        }).success(function (data) {
            $scope.signInInfoData = data;
        });
    };
    $scope.firstLoad = function () {
        $scope.getLatestDate().then(function (date) {
            $scope.getSignInInfo(date);
            $scope.getDutyStudent(date);
        });
    };
    $scope.getDutyStudent = function (date) {
        $http({
            method: 'GET',
            url: '/api/common_api/duty_students/'+date
        }).success(function (data) {
            $scope.dutyStudentData = data;
        });
    };
    $scope.checkOnTime = function (order, tsp) {
        var parser = datetime("yyyy-MM-dd HH:mm:ss");
        switch (order){
            case 1:
                parser.parse($scope.currentDate + ' 09:00:00');
                return parser.getDate() > new Date(tsp);
            case 2:
                parser.parse($scope.currentDate + ' 11:30:00');
                return parser.getDate() < new Date(tsp);
            case 3:
                parser.parse($scope.currentDate + ' 14:00:00');
                return parser.getDate() > new Date(tsp);
            case 4:
                parser.parse($scope.currentDate + ' 17:00:00');
                return parser.getDate() < new Date(tsp);
            case 5:
                parser.parse($scope.currentDate + ' 18:30:00');
                return parser.getDate() > new Date(tsp);
            case 6:
                parser.parse($scope.currentDate + ' 21:30:00');
                return parser.getDate() < new Date(tsp);
            default:
                return false;
        }
    };
    $scope.setTrStyle = function (order, tsp) {
        if(tsp==null){
            return '';
        }
        if($scope.checkOnTime(order, tsp)){
            return 'warning';
        }
        else{
            return 'info';
        }
    };
    $scope.copyStudent = function(student){
        $scope.askForAbsenceStudent.studentId = student.studentId;
        $scope.askForAbsenceStudent.absenceReason = student.absenceReason;
        $scope.askForAbsenceStudent.operDate = $scope.currentDate;
        $scope.askForAbsenceStudent.startAbsence = null;
        $scope.askForAbsenceStudent.endAbsence = null;
    };
    $scope.copySignInItem = function (id, order) {
        $scope.signInItem.studentId = id;
        $scope.signInItem.signInOrder = order;
        $scope.signInItem.operDate = $scope.currentDate;
    };
    $scope.signInAction = function () {
        // Generate the image data
        $scope.signInItem.imageData = document.getElementById("myCanvas").toDataURL("image/png");
        $scope.signInItem.imageData = $scope.signInItem.imageData.replace(/^data:image\/(png|jpg);base64,/, "");

        $http({
            method: 'POST',
            url: "/api/sign_in_info_api/sign_in_info/addition",
            data: $scope.signInItem
        }).success(function (data) {
            alert("签到成功！");
            clearArea();
            $scope.getSignInInfo($scope.currentDate);
        }).error(function () {
            alert("请先登录！");
        });
    };
    $scope.askForAbsence = function () {
        $http({
            method: 'POST',
            url: "/api/sign_in_info_api/absences/addition/",
            data: $scope.askForAbsenceStudent
        }).success(function (data) {
            alert("请假成功！");
            $scope.getSignInInfo($scope.currentDate);
        }).error(function () {
            alert("请先登录！");
        });
    };
    $(function () {
        $scope.currentDate = "";
        $scope.signInInfoData = {};
        $scope.askForAbsenceStudent = {};
        $scope.signInItem = {};
        $scope.dutyStudentData = {};
        InitThis();
        $scope.firstLoad();
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
    }).when('/sign_in_action', {
        controller: 'sign_in_action_ctrl',
        templateUrl: 'sign_in_action'
    });
}]);