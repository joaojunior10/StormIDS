'use strict';

/*
 * ng-c3 - C3 charts directive
 */

function ngC3() {
  function isEmptyJSON(json) {
    for (var attr in json) {
      return false;
    }
    return true;
  }

  function pluck(arrayObject, attr) {
    return arrayObject.map(function (el) {
      if (!el.hasOwnProperty(attr)) {
        console.error('Inconsistent Data');
        console.error('Verify your data, must be like one of these options \n' +
          'data = [{x: value, y: value},...,{x: value, y: value}] \n' +
          'or data = [[valueX, valueY],...,[valueX, valueY]]');
      }
      return el[attr];
    });
  }

  function merge(firstObject, secondObject) {
    for (var attr in secondObject) {
      if (!firstObject.hasOwnProperty(attr)) {
        firstObject[attr] = secondObject[attr];
      }
    }
  }

  function getData(series) {
    var xs = {},
      types = {},
      columns = [],
      x = [],
      y = [],
      yAxis = {};

    series.forEach(function (s) {
      if (s.yAxis) {
        yAxis[s.name] = s.yAxis;
      }

      if (s.type) {
        types[s.name] = s.type;
      }

      xs[s.name] = 'x' + s.name;

      if (s.data[0] instanceof Array) {
        x = pluck(s.data, 0);
        y = pluck(s.data, 1);
      } else if (s.data[0] instanceof Object) {
        x = pluck(s.data, 'x');
        y = pluck(s.data, 'y');
      } else {
        x = [];
        y = [];
        console.error('Verify your data, must be like one of these options \n' +
          'data = [{x: value, y: value},...,{x: value, y: value}] \n' +
          'or data = [[valueX, valueY],...,[valueX, valueY]]');
      }

      x.unshift('x' + s.name);
      y.unshift(s.name);

      columns.push.apply(columns, [x, y]);

    });

    var data = {
      columns: columns,
      xs: xs
    };

    if (!isEmptyJSON(types)) {
      data.types = types;
    }

    if (!isEmptyJSON(yAxis)) {
      data.axes = yAxis;
    }

    return data;
  }

  function getPiesData(series) {
    var columns = [];

    series.forEach(function (s) {
      var data = s.data.map(function (el) {
        if (el instanceof Array) {
          return el[1];
        } else if (el instanceof Object) {
          return el.y;
        } else {
          return el;
        }
      });
      data.unshift(s.name);
      columns.push(data);
    });

    return columns;
  }

  return {
    restrict: 'AE',
    template: '<div></div>',
    scope: {
      series: '=',
      options: '=',
      chartId: '@'
    },
    link: function (scope, element) {

      var chartElement = element[0].childNodes[0];
      chartElement.id = scope.chartId;

      scope.$watch('[series, options]', function (changes) {
        changes[1] = changes[1] || {};
        var body = {};
        var typeChart = changes[1].type || 'line';

        switch (typeChart) {
        case 'donut':
        case 'pie':
        case 'gauge':
          body.data = {
            type: typeChart,
            columns: getPiesData(changes[0])
          };
          merge(body, changes[1]);
          break;
        default:
          body.data = getData(changes[0]);
          body.data.type = typeChart;
          if (changes[1]) {
            merge(body, changes[1]);
            body.data.groups = changes[1].groups || [];
            /*jslint evil: true */
            body.data.onclick = changes[1].onclick || new Function();
          }
          break;
        }
        body.bindto = scope.chartId ? '#' + scope.chartId : '#chart';
        var chart = c3.generate(body);

      }, true);
    }
  };
}

angular.module('urbanApp').directive('ngC3', ngC3);
