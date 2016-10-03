var app = angular.module('signInSys', []);
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