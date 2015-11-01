'use strict';

function matchesCtrl($scope, $http) {
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    $scope.maxSize = 5;
    $scope.total = 1000;

    $http.get('/api/matches').success(function(data) {
        $scope.matches = data;
        $scope.total = $scope.matches.length;

    });

    $scope.numberOfPages=function(){
        return Math.ceil($scope.total/$scope.pageSize);
    }
}

angular
  .module('urbanApp')
  .controller('matchesCtrl', ['$scope','$http', matchesCtrl]);
