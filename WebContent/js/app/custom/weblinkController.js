(function () {
    'use strict';
    
    angular.module('app').controller('weblinkCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {
  	 	   /**
  	        * Start create Weblink
  	        */
  	 	    
  	 	    $scope.weblinkSelected=false;
  	        $scope.weblinkSaved=false;
  	        $scope.weblinkSaveSubmitted=false;
  	        $scope.theUser = $cookieStore.get('theUser');
  	         
  	         var weblinkUploader = $scope.weblinkUploader = new FileUploader({
  	 	            url: 'http://www.arelbou.com/service/weblink/receiveFile'
  	 	        });
  	 	        
  	 	
  	 	        // FILTERS

  	       weblinkUploader.filters.push({
  	 	            name: 'customFilter',
  	 	            fn: function(item /*{File|FileLikeObject}*/, options) {
  	 	                return this.queue.length < 10;
  	 	            }
  	 	        });

  	 	        // CALLBACKS
  	     weblinkUploader.onBeforeUploadItem = function(item) {
  	 	            console.info('onBeforeUploadItem', item);
  	 	            item.formData.push({weblinkId: $scope.theWeblink.id});
  	 	            $log.info(item);
  	 	            
  	 	        };
  	 	        
  	 	        
  	 	    $scope.createWeblink = function() {  
  	 	      	$scope.weblinkSaveSubmitted=true;	      	
  	 	      	$http({ method: 'POST', url: 'http://www.arelbou.com/service/weblink/createWeblink', data: this.theWeblink }).
  	 	      	success(function (data, status, headers, config) {
  	                    $log.info("Call Create Weblink Successful"); 
  	                    $scope.weblinkSelected=true;
  	                    $scope.weblinkSaved=true;
  	                  	$cookieStore.put('theWeblink',data);
  	                 	$scope.theWeblink = data;
  	                 	$scope.theWeblinkMessage='Weblink enregistree avec succes';
  	                    $log.info($scope);
  	                             
  	            }).error(function (data, status, headers, config) {
  	                    $log.info("Call Create Weblink Failed");
  	                    $cookieStore.put('theWeblink','');
  	                    $scope.theWeblinkMessage='Weblink ne peut etre enregistrer. Essayer plus tard';
  	                    $scope.theWeblink = null; 
  	                    $scope.weblinkSaved = false;
  	                    $scope.weblinkSelected = false;
  	                    $log.info($scope);
  	                   
  	                });
  	   
  	           };
  	           /**
  	 	       * End Create Weblink
  	 	       */
  	        
  	     /**
     * Start select Weblink
     * 
     */
          $scope.selectWeblink = function(aWeblink) {
        	  
        	  $scope.theWeblink = aWeblink;
        	  
        	  
        	  $scope.weblinkSelected = true;
              $log.info($scope.theWeblink.status); 
             	  	              
          };
	 /**
	  * End select Weblink
	  */
  	        
  	/**
     * Start clear Weblink
     * 
     */
          $scope.clearWeblink = function() {
            	 
        	  $scope.theWeblink = '';  	  	   	  	  	                    	  
        	  $scope.weblinkSelected = false;
              $log.info($scope); 
             	  	              
          };
	 /**
	  * End clear Weblink
	  */
                   
 	  	  	                      
/**
 * Start get Weblinks
 * Get the list of Weblinks
 */
  $scope.getAllWeblinks = function() {
        
       $http({ method: 'POST', url: 'http://www.arelbou.com/service/weblink/getAllWeblinks', data: null }).
       success(function (data, status, headers, config) {
                $log.info("Call get All Weblink Successful"); 
            	$scope.weblinks = data;
                $log.info($scope.weblinks);
                
       }).error(function (data, status, headers, config) {
                $log.info("Call get All Weblink Failed");
                $log.info($scope);
       });

  };
  
  /**
   * Start get Weblinks
   * Get the list of Weblinks
   */
    $scope.getActiveWeblinks = function() {
          
         $http({ method: 'POST', url: 'http://www.arelbou.com/service/weblink/getActiveWeblinks', data: null }).
         success(function (data, status, headers, config) {
                  $log.info("Call get Active Weblink Successful"); 
              	  $scope.activeWeblinks = data;
                  $log.info($scope.activeWeblinks);
                  
         }).error(function (data, status, headers, config) {
                  $log.info("Call get Active Weblink Failed");
         });

    };
    
    $scope.openInNewTab = function(link){
        $window.open(link, '_blank');
    };
    
 /**
  * End get active Weblinks
  */
  
   var url = $location.url();
	$log.info('URL='+url);
	
	if(url=='/pages/weblinks'){
		$scope.getAllWeblinks();
	} 
	
	$scope.getActiveWeblinks();
}])

})();