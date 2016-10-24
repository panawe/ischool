(function () {
    'use strict';
    
    angular.module('app').controller('sponsorCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state, $window, $cookieStore, $log, $http, FileUploader, $location, moment) {
  	 	   /**
  	        * Start create Sponsor
  	        */
  	 	    
  	 	    $scope.sponsorSelected=false;
  	         $scope.sponsorSaved=false;
  	         $scope.sponsorSaveSubmitted=false;
  	       $scope.theUser = $cookieStore.get('theUser');
  	         
   	 	        
  	 	    $scope.createSponsor = function() {  
  	 	      	$scope.sponsorSaveSubmitted=true;	      	
  	 	      	$http({ method: 'POST', url: 'http://www.arelbou.com/service/sponsor/createSponsor', data: this.theSponsor }).
  	 	      	success(function (data, status, headers, config) {
  	                     $log.info("Call Create Sponsor Successful"); 
  	                     $scope.sponsorSelected = true;
  	               $scope.sponsorSaved = true;
  	               //this.theEvent='';
  	                  	$cookieStore.put('theSponsor',data);
  	                 	$scope.theSponsor=data;
  	             $scope.theSponsorMessage='Sponsor enregistree avec succes';
  	                     $log.info($scope);
  	                     
  	                     
  	            }).error(function (data, status, headers, config) {
  	                     $log.info("Call Create Sponsor Failed");
  	                     $cookieStore.put('theSponsor','');
  	               $scope.theSponsorMessage='Sponsor ne peut etre enregistrer. Essayer plus tard';
  	                         $scope.theSponsor = null; 
  	                   $scope.sponsorSaved = false;
  	                   $scope.sponsorSelected = false;
  	                         $log.info($scope);
  	                   
  	                });
  	   
  	           };
  	           /**
  	 	       * End Create Sponsor
  	 	       */
  	        
  	     /**
     * Start select Sponsor
     * 
     */
          $scope.selectSponsor = function(aSponsor) {        	  
        	  $scope.theSponsor = aSponsor; 
        	  $scope.sponsorSelected = true;
        	  $log.info("$scope.sponsorSelected= "+$scope.sponsorSelected); 
             // $log.info($scope.theSponsor.status); 
             	  	              
          };
	 /**
	  * End select Sponsor
	  */
  	        
     /**
     * Start clear Sponsor
     * 
     */
          $scope.clearSponsor = function() {
            	 
        	  $scope.theSponsor = '';  	  	   	  	  	                    	  
        	  $scope.sponsorSelected = false;
              $log.info($scope); 
             	  	              
          };
	 /**
	  * End clear Sponsor
	  */
   
	/**
	 * Start Delete Sponsor
	 * 
	 */
	$scope.deleteSponsor = function(aSponsor) {
		aSponsor.deleted = false;
		$http(
				{
					method : 'POST',
					url : 'http://www.arelbou.com/service/sponsor/deleteSponsor',
					data : aSponsor
				})
				.success(
						function(data, status, headers, config) {
							$log.info("Call deleteSponsor Successful");
							aSponsor.deleted = true;
							$scope.theSponsor = '';  	  	   	  	  	                    	  
				        	$scope.sponsorSelected = false;
							$log.info($scope);

						})
				.error(
						function(data, status,
								headers, config) {
							$log
									.info("Call deleteSponsor Failed");
							$log.info($scope);

						});

	};
	/**
	 * End Delete
	 */

 	  	  	                      
/**
 * Start get Sponsors
 * Get the list of Sponsors
 */
  $scope.getAllSponsors = function() {
        
       $http({ method: 'POST', url: 'http://www.arelbou.com/service/sponsor/getAllSponsors', data: null }).
       success(function (data, status, headers, config) {
                $log.info("Call get All Sponsors Successful"); 
            	$scope.sponsors = data;
                $log.info($scope.sponsors);
             //$cookieStore.put('projects',data);
                
       }).error(function (data, status, headers, config) {
                $log.info("Call get All Sponsors Failed");
                $log.info($scope);
             //$cookieStore.put('projects',null);
       });

  };
 /**
  * End get all Sponsors
  */
  
  
  /**
   * Begin the Advertisement section
   */
  
		/**
		 * Start File uploader
		 */
	
		var advertisementUploader = $scope.advertisementUploader = new FileUploader(
		{
			url : 'http://www.arelbou.com/service/marketing/receiveFile'
		});
	
		// FILTERS
	
		advertisementUploader.filters.push({
			name : 'customFilter',
			fn : function(
					item /* {File|FileLikeObject} */,
					options) {
				return this.queue.length < 10;
			}
		});
	
		// CALLBACKS
		advertisementUploader.onBeforeUploadItem = function(
				item) {
			console.info('onBeforeUploadItem', item);
			item.formData.push({
				advertisementId : $scope.theAdvertisement.id
			});
			$log.info(item);
	
		};

  
  		$scope.advertisementSelected = false;
	    $scope.advertisementSaved = false;
	    $scope.advertisementSaveSubmitted = false;
	        
 	    $scope.createAdvertisement = function() {  
      	$scope.advertisementSaveSubmitted = true;	   
      	this.theAdvertisement.sponsorId = this.theSponsor.id
      	$http({ method: 'POST', url: 'http://www.arelbou.com/service/marketing/createMarketing', data: this.theAdvertisement }).
      	success(function (data, status, headers, config) {
            $log.info("Call Create Advertisement Successful"); 
            $scope.advertisementSelected = true;
            $scope.advertisementSaved = true;
          	$cookieStore.put('theAdvertisement', data);
         	$scope.theAdvertisement = data;
         	$scope.theAdvertisementMessage = 'Publicite enregistree avec succes';
            $log.info($scope);
                         
        }).error(function (data, status, headers, config) {
            $log.info("Call Create Advertisement Failed");
            $cookieStore.put('theAdvertisement','');
            $scope.theAdvertisementMessage = 'Publicite ne peut etre enregistrer. Essayer plus tard';
            $scope.theAdvertisement = null; 
            $scope.advertisementSaved = false;
            $scope.advertisementSelected = false;
            $log.info($scope);
           
        });
   
      };
      /**
       * End Create Advertisement
       */
  	  
      /**
      * Start select Sponsor
      * 
      */
      $scope.selectAdvertisement = function(aAdvertisement) {
    	  $scope.theAdvertisement = aAdvertisement;
       	  $scope.advertisementSelected = true;
              	  	              
      };
 	 /**
 	  * End select Advertisement
 	  */
   	        
      /**
      * Start clear Advertisement
      * 
      */
       $scope.clearAdvertisement = function() {             	 
     	  $scope.theAdvertisement = '';  	  	   	  	  	                    	  
     	  $scope.advertisementSelected = false;
          $log.info($scope); 
          	  	              
       };
 	 /**
 	  * End clear Sponsor
 	  */

      /**
       * Start get Advertisements by sponsor
       * Get the list of Advertisements by sponsor
       */
        $scope.getAllAdvertisementsBySponsor = function() {
              
             $http({ method: 'POST', url: 'http://www.arelbou.com/service/marketing/getAllMarketingsBySponsor', data: this.theSponsor.id }).
             success(function (data, status, headers, config) {
                    $log.info("Call get All Advertisement for sponsor Successful"); 
                  	$scope.advertisements = data;
                    $log.info($scope.advertisements);
             }).error(function (data, status, headers, config) {
                    $log.info("Call get All get All Advertisements By Sponsor Failed");
                    $log.info($scope);
             });
        };
       /**
        * End get all Sponsors
        */
        
        
       var url = $location.url();
		$log.info('URL='+url);
		
		if(url == '/pages/sponsors'){
			$scope.getAllSponsors();
		}
           	  	  	                  
}])

})();