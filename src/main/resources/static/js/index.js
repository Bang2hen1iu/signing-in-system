var sign_in_app = angular.module('signInSys', ['angular-toArrayFilter', 'ngRoute']);
sign_in_app.controller('navbar_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.login = function () {
        $http({
            method: 'POST',
            url: "/api/sign_in_info_api/admin/validation",
            data: login_data
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
sign_in_app.controller('sign_in_info_ctrl', ['$scope', '$http', '$q', function ($scope, $http, $q) {
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
        });
    };
    $(function () {
        $scope.currentDate = "";
        $scope.signInInfoData = {};
        $scope.firstLoad();
    });
}]);
sign_in_app.controller('rank_list_ctrl', ['$scope', '$http', function ($scope, $http) {
    $scope.getRankListData = function () {
        $http({
            method: 'GET',
            url: "/api/common_api/statistics/sum"
        }).success(function (data) {
            $scope.rank_list_data = data;
        });
    };
    $scope.getProgressBarWidth = function (stayLabTime) {
        return {'width':(stayLabTime/50*100)+'%'};
    };
    $(function () {
        $scope.rank_list_data = {};
        $scope.getRankListData();

    });
}]);
sign_in_app.controller('sign_in_action_ctrl', ['$scope', '$http', function ($scope, $http) {
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
            url: "/student/student_info"
        }).success(function (data) {
            $scope.user_profile_data = data;
        });
    };
    $scope.modifyPassword = function () {
        $http({
            method: 'PUT',
            url: "/student/" + $scope.new_password + "/password_modification/"
        }).success(function (data) {
            $scope.new_password = '';
        });
    };
    $(function () {
        $scope.list = false;
        $scope.addition = false;
        $scope.user_profile_data = {};
        $scope.new_password = '';
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