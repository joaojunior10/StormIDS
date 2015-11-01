'use strict';

/*
 * offscreen - Off canvas sidebar directive
 */

function offscreen($rootScope, $timeout) {
  return {
    restrict: 'EA',
    replace: true,
    transclude: true,
    templateUrl: 'views/directives/toggle-offscreen.html',
    link: function (scope, element, attrs) {
      scope.offscreenDirection = attrs.move;
    },
    controller: function ($scope, $element, $timeout) {
      var dir,
        offscreenDirectionClass;

      $scope.offscreen = function () {
        dir = $scope.offscreenDirection ? $scope.offscreenDirection : 'ltr';

        if ($scope.app.layout.isChatOpen) {
          $scope.app.layout.isChatOpen = !$scope.app.layout.isChatOpen;
        }

        if (dir === 'rtl' || angular.element('.app').hasClass('layout-right-sidebar')) {
          offscreenDirectionClass = 'move-right';
        } else {
          offscreenDirectionClass = 'move-left';
        }

        if ($scope.app.layout.isOffscreenOpen) {
          angular.element('.app').removeClass('offscreen move-left move-right');
          $scope.app.layout.isOffscreenOpen = false;
        } else {
          angular.element('.app').addClass('offscreen ' + offscreenDirectionClass);
          $scope.app.layout.isOffscreenOpen = true;
        }
      };
    }
  };
}

angular.module('urbanApp').directive('offscreen', offscreen);
