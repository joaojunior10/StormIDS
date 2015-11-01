'use strict';

/*
 * scroll-up - Scroll to top of page directive
 */
function scrollUp() {
  return {
    link: function (scope, element) {
      element.on('click', function () {
        angular.element('body,html').stop().animate({
          scrollTop: 0
        }, 300);
        return false;
      });
    }
  };
}

angular.module('urbanApp').directive('scrollUp', scrollUp);
