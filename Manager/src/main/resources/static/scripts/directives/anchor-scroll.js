'use strict';

/*
 * anchor-scroll - scroll to id directive
 */

function anchorScroll($anchorScroll, $location) {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      element.bind('click', function() {
        var id = attrs.anchorScroll;
        $location.hash(id);
        $anchorScroll();
      });
    }
  };
}

angular.module('urbanApp')
.run(['$anchorScroll', function($anchorScroll) {
  $anchorScroll.yOffset = 55;
}])
.directive('anchorScroll', anchorScroll);
