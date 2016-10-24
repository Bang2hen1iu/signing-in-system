var sign_in_app = angular.module('signInSys', ['angular-toArrayFilter', 'ngRoute', 'datetime']);
sign_in_app.controller('navbar_ctrl',['$scope',function ($scope) {

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
        for(var regTpl in $scope.fingerPrintData){
            if(zkonline.MatchFinger(regTpl.token,verifyTpl)){
                $scope.signInData.fingerprint = regTpl.token;
                break;
            }
        }
        if($scope.signInData.fingerprint==null){
            alert('error!');
            return;
        }
        $http({
            method: 'POST',
            url: "/api/sign_in_info/addition",
            data:$scope.signInData
        }).success(function () {
            alert('success');
        }).error(function () {
            alert('fail');
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
            url: '/api/students/duty_students/'+date
        }).success(function (data) {
            $scope.dutyStudentData = data;
        });
    };
    $scope.getSignInInfo = function (date) {
        $http({
            method: 'GET',
            url: "/api/sign_in_info/"+date
        }).success(function (data) {
            $scope.signInInfoData = data;
        });
    };
    $scope.firstLoad = function () {
        $scope.getFingerPrint();
        $scope.getLatestDate().then(function (date) {
            var parser = datetime("yyyy-MM-dd");
            parser.parse(date);
            $scope.currentDate = parser.getDate();
            $scope.currentWeekday = $scope.weekday[$scope.currentDate.getDay()];
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
    $scope.setBarClass = function (bar) {
        var baseClass = "progress-bar";
        if(bar==null){
            return '';
        }
        if(bar.type==0){
            return baseClass+' palette palette-orange';
        }
        else if(bar.type==1){
            return baseClass+' palette palette-carrot';
        }
        else if(bar.type==2){
            return baseClass+' palette palette-peter-river';
        }
        else if(bar.type==3){
            return baseClass+' palette palette-peter-river';
        }
        else if(bar.type==4){
            return baseClass+' palette palette-emerald';
        }
        else{
            return '';
        }
    };
    $scope.getTimeSegmentWidth = function (bar) {
        return {'width':bar.width};
    };
    $(function () {
        $scope.firstLoad();
        $scope.weekday = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
        $('#dateInput').on('change',function () {
            var parser = datetime("yyyy-MM-dd");
            parser.setDate($scope.currentDate);
            $scope.currentWeekday = $scope.weekday[$scope.currentDate.getDay()];
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
            url: "/api/statistics/sum"
        }).success(function (data) {
            $scope.maxStayLabTime = Math.max.apply(Math,data.map(function(item){return item.stayLabTime;}));
            $scope.rank_list_data = data;
        });
    };
    $scope.getProgressBarWidth = function (stayLabTime) {
        return {'width':(stayLabTime/($scope.maxStayLabTime*1.3)*100)+'%'};
    };
    $(function () {
        $scope.getRankListData();
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
    });
}]);