'use strict';

/*
 * panel-control-remove - panel toolbar remove directive
 */

function panelControlRemove() {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      element.bind('click', function() {
        var parent = angular.element(element).closest('.panel');
        parent.addClass('animated fadeOut');
        parent.bind('animationend webkitAnimationEnd oAnimationEnd MSAnimationEnd', function () {
          parent.remove();
        });
      });
    }
  };
}

angular.module('urbanApp').directive('panelControlRemove', panelControlRemove);
