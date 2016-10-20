var app = angular.module('signInSys', []);
app.controller('signInSysCtrl', ['$scope', '$http', function ($scope,$http) {
    $scope.signInData= {};
    $scope.processForm= function () {
        $http({
            method: 'POST',
            url: '/api/admin/validation',
            data:$scope.signInData
        }).success(function () {
            $scope.errorMessage = "";
            $scope.successMessage = "成功";
            window.location = '/admin';
        }).error(function () {
            $scope.errorMessage
                = "账号或密码错误";
            $scope.successMessage
                = "";
        })
    };
}]);