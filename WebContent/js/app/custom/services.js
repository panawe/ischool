app.service('userService',['$log','$scope', '$cookieStore' , function($log, $scope, $cookieStore){
	
	var service={};
	
	service.login = login;
	service.setCredentials=setCredentials;
	service.clearCredentials=clearCredentials;
	
	return service;
	
	function login(userName, password, callback){
		
		
	}
	
    function setCredentials(username, password) {
        var authdata =  username + ':' + password;

        $rootScope.globals = {
            currentUser: {
                username: username,
                authdata: authdata
            }
        };

        $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
        $cookieStore.put('globals', $rootScope.globals);
    }

    function clearCredentials() {
        $scope.globals = {};
        $cookieStore.remove('globals');
        $http.defaults.headers.common.Authorization = 'Basic';
    }
}])