'use strict';

/*
 * chosen - chosen select directive alternative to ui-jq
 */

function chosen() {
  return {
    restrict: 'EA',
    link: function (scope, element, attrs) {
      // update the select when data is loaded
      scope.$watch(attrs.chosen, function () {
        element.trigger('chosen:updated');
      });

      // update the select when the model changes
      scope.$watch(attrs.ngModel, function () {
        element.trigger('chosen:updated');
      });

      element.chosen();
    }
  };
}

angular.module('urbanApp').directive('chosen', chosen);
