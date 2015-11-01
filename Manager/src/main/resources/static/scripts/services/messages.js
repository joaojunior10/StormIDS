'use strict';

function messages($q, $http) {
  var output = {};
  var messageUrl = 'data/messages.json';

  output.getAll = function () {
    output.messages = [];
    var deferred = $q.defer();
    return $http.get(messageUrl)
      .success(function (data) {
        output.messages = data;
        deferred.resolve(data);
      })
      .error(function (data) {
        deferred.reject(data);
      });
    //return deferred.promise;
  };

  output.getById = function (messageId) {
    if (messageId) {
      output.message = [];
      var deferred = $q.defer();
      return $http.get('data/message/' + messageId + '.json')
        .success(function (data) {
          output.message = data;
          deferred.resolve(data);
        })
        .error(function (data) {
          deferred.reject(data);
        });
      //return deferred.promise;
    }
  };

  return output;
}

angular
  .module('urbanApp')
  .factory('messages', ['$q', '$http', messages]);
