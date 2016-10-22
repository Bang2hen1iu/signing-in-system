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
sign_in_app.controller('sign_in_action_ctrl', ['$scope', '$http', '$q', 'datetime', '$timeout', function ($scope, $http, $q, datetime, $timeout) {

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