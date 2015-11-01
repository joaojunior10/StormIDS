'use strict';

/*
 * quickLaunch - quick launch panel directive
 */
function quickLaunch($timeout) {
  return {
    restrict: 'EA',
    link: function (scope, element, attrs) {
      var body = angular.element('body');
      element.on('click', function (e) {
        e.preventDefault();
        if (!body.hasClass('layout-quick-launch-panel')) {
          body.css({transition: '300ms cubic-bezier(.7,0,.3,1)'}).addClass('layout-quick-launch-panel stop-scrolling');
        }
        else {
          body.removeClass('layout-quick-launch-panel');
          $timeout(function () {
            body.removeClass('stop-scrolling').css({transition: ''});
          }, 300);
        }
        window.scrollTo(0, 0);
      });
      angular.element('.quick-launch-panel').find('a').on('click', function (e) {
        if (body.hasClass('layout-quick-launch-panel')) {
          body.removeClass('layout-quick-launch-panel');
          $timeout(function () {
            body.removeClass('stop-scrolling').css({transition: ''});
          }, 300);
        }
      });
    }
  };
}

angular.module('urbanApp').directive('quickLaunch', quickLaunch);
