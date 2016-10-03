var app = angular.module('signInSys', []);
app.controller('signInSysCtrl', ['$scope', '$http', function ($scope,$http) {
    //登录ajax部分
    $scope.sign_in_data= {};
    $scope.processForm= function () {
        $http({
            method: 'POST',
            url: '/api/admin_api/admin/validation',
            data:$scope.sign_in_data
        }).success(function () {
            $scope.error_message = "";
            $scope.success_message = "成功";
            window.location = '/admin';
        }).error(function () {
            $scope.error_message
                = "账号或密码错误";
            $scope.success_message
                = "";
        })
    };
}]);