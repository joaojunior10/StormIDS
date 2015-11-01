'use strict';

/*
 * panel-control-collapse - panel toolbar collapse directive
 */

function panelControlCollapse() {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      element.bind('click', function() {
        var parent = angular.element(element).closest('.panel');
        parent.toggleClass('panel-collapsed');
      });
    }
  };
}

angular.module('urbanApp').directive('panelControlCollapse', panelControlCollapse);
