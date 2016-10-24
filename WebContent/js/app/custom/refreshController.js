(function () {
    'use strict';
    
    angular.module('app').controller('refreshCtrl', function($scope, $interval, $log, $http){
		var c = 0;
		$interval(function(){
			$http({ method: 'POST', url: 'http://www.arelbou.com/service/user/getMessagingUsers', data : null }).
	        success(function (data, status, headers, config) {
	        	$log.info("Call Successful"); 
	            $scope.onlineMembers = data;
	            $log.info($scope);
	                 
	        }).error(function (data, status, headers, config) {
	            $log.info("Call Failed");
	        });
			
			$http({ method: 'POST', url: 'http://www.arelbou.com/service/user/getGuestCount', data : null }).
	        success(function (data, status, headers, config) {
	        	$log.info("Call Successful"); 
	            $scope.guestCount = data;
	            $log.info($scope);
	                 
	        }).error(function (data, status, headers, config) {
	            $log.info("Call Failed");
	        });
			
		},10000);
	});
})();