'use strict';

angular
  .module('urbanApp')
  .run(['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
      $rootScope.$state = $state;
      $rootScope.$stateParams = $stateParams;
      $rootScope.$on('$stateChangeSuccess', function () {
        window.scrollTo(0, 0);
      });
      FastClick.attach(document.body);
        },
    ])
  .config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {

      // For unmatched routes
      $urlRouterProvider.otherwise('/matches');

      // Application routes
      $stateProvider
        .state('app', {
          abstract: true,
          templateUrl: 'views/common/layout.html',
        })
          .state('app.matches', {
              url: '/matches',
              templateUrl: 'views/matches.html',
              resolve: {
                  deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                      return $ocLazyLoad.load([
                          {
                              insertBefore: '#load_styles_before',
                              files: [
                                  'vendor/chosen_v1.4.0/chosen.min.css',
                                  'vendor/datatables/media/css/jquery.dataTables.css'
                              ]
                          },
                          {
                              serie: true,
                              files: [
                                  'vendor/chosen_v1.4.0/chosen.jquery.min.js',
                                  'vendor/datatables/media/js/jquery.dataTables.js',
                                  'scripts/extentions/bootstrap-datatables.js'
                              ]
                          }]).then(function () {
                          return $ocLazyLoad.load('scripts/controllers/matches.js');
                      });
                  }]
              },
              data: {
                  title: 'Matches',
              }
          })
        }
    ])
  .config(['$ocLazyLoadProvider', function ($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
      debug: false,
      events: false
    });
    }]);
