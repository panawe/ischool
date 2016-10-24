(function () {
    'use strict';
    
    angular.module('app').controller('announceCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {
  	 	   /**
  	        * Start create Announce
  	        */
  	 	    
  	 	    $scope.announceSelected=false;
  	        $scope.announceSaved=false;
  	        $scope.announceSaveSubmitted=false;
  	        $scope.theUser = $cookieStore.get('theUser');
  	         
  	         var announceUploader = $scope.announceUploader = new FileUploader({
  	 	            url: 'http://www.arelbou.com/service/announce/receiveFile'
  	 	        });
  	 	        
  	 	
  	 	        // FILTERS

  	 	      announceUploader.filters.push({
  	 	            name: 'customFilter',
  	 	            fn: function(item /*{File|FileLikeObject}*/, options) {
  	 	                return this.queue.length < 10;
  	 	            }
  	 	        });

  	 	        // CALLBACKS
  	 	    announceUploader.onBeforeUploadItem = function(item) {
  	 	            console.info('onBeforeUploadItem', item);
  	 	            item.formData.push({announceId: $scope.theAnnounce.id});
  	 	            $log.info(item);
  	 	            
  	 	        };
  	 	        
  	 	        
  	 	    $scope.createAnnounce = function() {  
  	 	      	$scope.announceSaveSubmitted=true;	      	
  	 	      	$http({ method: 'POST', url: 'http://www.arelbou.com/service/announce/createAnnounce', data: this.theAnnounce }).
  	 	      	success(function (data, status, headers, config) {
  	                    $log.info("Call Create Announce Successful"); 
  	                    $scope.announceSelected=true;
  	                    $scope.announceSaved=true;
  	                  	$cookieStore.put('theAnnounce',data);
  	                 	$scope.theAnnounce = data;
  	                 	$scope.theAnnounceMessage='Announce enregistree avec succes';
  	                    $log.info($scope);
  	                             
  	            }).error(function (data, status, headers, config) {
  	                    $log.info("Call Create Announce Failed");
  	                    $cookieStore.put('theAnnounce','');
  	                    $scope.theAnnounceMessage='Announce ne peut etre enregistrer. Essayer plus tard';
  	                    $scope.theAnounce = null; 
  	                    $scope.announceSaved = false;
  	                    $scope.announceSelected = false;
  	                    $log.info($scope);
  	                   
  	                });
  	   
  	           };
  	           /**
  	 	       * End Create Announce
  	 	       */
  	        
  	     /**
     * Start select Announce
     * 
     */
          $scope.selectAnnounce = function(aAnnounce) {
        	  
        	  $scope.theAnnounce = aAnnounce;
        	  
        	  
        	  $scope.announceSelected=true;
              $log.info($scope.theAnnounce.status); 
             	  	              
          };
	 /**
	  * End select Announce
	  */
  	        
  	/**
     * Start clear Announce
     * 
     */
          $scope.clearAnnounce = function() {
            	 
        	  $scope.theAnnounce = '';  	  	   	  	  	                    	  
        	  $scope.announceSelected = false;
              $log.info($scope); 
             	  	              
          };
	 /**
	  * End clear Announce
	  */
                   
 	  	  	                      
/**
 * Start get Announces
 * Get the list of Announces
 */
  $scope.getAllAnnounces = function() {
        
       $http({ method: 'POST', url: 'http://www.arelbou.com/service/announce/getAllAnnounces', data: null }).
       success(function (data, status, headers, config) {
                $log.info("Call get All Announces Successful"); 
            	$scope.announces = data;
                $log.info($scope.announces);
                
       }).error(function (data, status, headers, config) {
                $log.info("Call get All Announce Failed");
                $log.info($scope);
       });

  };
 /**
  * End get all Announces
  */
  
   var url = $location.url();
	$log.info('URL='+url);
	
	if(url=='/pages/announces'){
		$scope.getAllAnnounces();
	}  	  	  	                  
}])

})();