'use strict';

/*
 * panel-control-refresh - panel toolbar refresh directive
 */

function panelControlRefresh($timeout) {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      element.bind('click', function() {
        var parent = angular.element(element).closest('.panel');
        parent.addClass('panel-refreshing');
        $timeout(function () {
          parent.removeClass('panel-refreshing');
        }, 3000);
      });
    }
  };
}

angular.module('urbanApp').directive('panelControlRefresh', panelControlRefresh);
