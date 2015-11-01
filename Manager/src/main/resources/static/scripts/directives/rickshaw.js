'use strict';

/*
 * rickshaw - rickshaw charts directive
 */
function rickshaw($compile, $window) {

  return {
    restrict: 'EA',
    scope: {
      options: '=rickshawOptions',
      series: '=rickshawSeries',
      features: '=rickshawFeatures'
    },
    // replace: true,
    link: function (scope, element) {
      var graphEl;
      var graph;
      var settings;

      function update() {
        if (!graph) {
          var mainEl = angular.element(element);
          mainEl.append(graphEl);
          mainEl.empty();
          graphEl = $compile('<div></div>')(scope);
          mainEl.append(graphEl);

          settings = angular.copy(scope.options);
          settings.element = graphEl[0];
          settings.series = scope.series;

          graph = new Rickshaw.Graph(settings);

          if (scope.features && scope.features.hover) {
            var hoverConfig = {
              graph: graph
            };
            hoverConfig.xFormatter = scope.features.hover.xFormatter;
            hoverConfig.yFormatter = scope.features.hover.yFormatter;
            hoverConfig.formatter = scope.features.hover.formatter;
            var hoverDetail = new Rickshaw.Graph.HoverDetail(hoverConfig);
            mainEl.find('.detail').addClass('inactive');
          }

          if (scope.features && scope.features.palette) {
            var palette = new Rickshaw.Color.Palette({
              scheme: scope.features.palette
            });
            for (var i = 0; i < settings.series.length; i++) {
              settings.series[i].color = palette.color();
            }
          }
          if (scope.features && scope.features.xAxis) {
            var xAxisConfig = {
              graph: graph
            };
            if (scope.features.xAxis.timeUnit) {
              var time = new Rickshaw.Fixtures.Time();
              xAxisConfig.timeUnit = time.unit(scope.features.xAxis.timeUnit);
            }
            var xAxis = new Rickshaw.Graph.Axis.Time(xAxisConfig);
            xAxis.render();
          }
          if (scope.features && scope.features.yAxis) {
            var yAxisConfig = {
              graph: graph
            };
            if (scope.features.yAxis.tickFormat) {
              yAxisConfig.tickFormat = Rickshaw.Fixtures.Number[scope.features.yAxis.tickFormat];
            }

            var yAxis = new Rickshaw.Graph.Axis.Y(yAxisConfig);
            yAxis.render();
          }
          if (scope.features && scope.features.legend) {
            var legendEl = $compile('<div></div>')(scope);
            mainEl.append(legendEl);

            var legend = new Rickshaw.Graph.Legend({
              graph: graph,
              element: legendEl[0]
            });
            if (scope.features.legend.toggle) {
              var shelving = new Rickshaw.Graph.Behavior.Series.Toggle({
                graph: graph,
                legend: legend
              });
            }
            if (scope.features.legend.highlight) {
              var highlighter = new Rickshaw.Graph.Behavior.Series.Highlight({
                graph: graph,
                legend: legend
              });
            }
          }
        } else {
          settings = angular.copy(scope.options, settings);
          settings.element = graphEl[0];
          settings.series = scope.series;

          settings.width = element.parent().width();

          graph.configure(settings);
        }

        graph.render();
      }

      var optionsWatch = scope.$watch('options', function (newValue, oldValue) {
        if (!angular.equals(newValue, oldValue)) {
          update();
        }
      }, true);
      var seriesWatch = scope.$watch(function (scope) {
        if (scope.features && scope.features.directive && scope.features.directive.watchAllSeries) {
          var watches = {};
          for (var i = 0; i < scope.series.length; i++) {
            watches['series' + i] = scope.series[i].data;
          }
          return watches;
        } else {
          return scope.series[0].data;
        }
      }, function (newValue, oldValue) {
        if (!angular.equals(newValue, oldValue)) {
          update();
        }
      }, true);
      var featuresWatch = scope.$watch('features', function (newValue, oldValue) {
        if (!angular.equals(newValue, oldValue)) {
          update();
        }
      }, true);

      scope.$on('$destroy', function () {
        optionsWatch();
        seriesWatch();
        featuresWatch();
      });

      var w = angular.element($window);

      scope.getWidth = function () {
        return window.innerWidth;
      };

      scope.$watch(scope.getWidth, function (newValue, oldValue) {
        update();
      });

      w.bind('resize', function () {
        scope.$apply();
      });

      update();
    },
    controller: function ($scope, $element, $attrs) {}
  };
}

angular.module('urbanApp').directive('rickshaw', rickshaw);
