(function () {
    'use strict';
    
    angular.module('app').controller('projectCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {
  	 	   /**
  	        * Start create Project
  	        */
  	 	    
  	 	    $scope.projectSelected=false;
  	         $scope.projectSaved=false;
  	         $scope.projectSaveSubmitted=false;
  	       $scope.theUser = $cookieStore.get('theUser');
  	         
  	         var projectUploader = $scope.projectUploader = new FileUploader({
  	 	            url: 'http://www.arelbou.com/service/project/receiveFile'
  	 	        });
  	 	        
  	 	
  	 	        // FILTERS

  	 	      projectUploader.filters.push({
  	 	            name: 'customFilter',
  	 	            fn: function(item /*{File|FileLikeObject}*/, options) {
  	 	                return this.queue.length < 10;
  	 	            }
  	 	        });

  	 	        // CALLBACKS
  	 	    projectUploader.onBeforeUploadItem = function(item) {
  	 	            console.info('onBeforeUploadItem', item);
  	 	            item.formData.push({projectId: $scope.theProject.id});
  	 	            $log.info(item);
  	 	            
  	 	        };
  	 	        
  	 	        
  	 	    $scope.createProject = function() {  
  	 	      	$scope.projectSaveSubmitted=true;	      	
  	 	      	$http({ method: 'POST', url: 'http://www.arelbou.com/service/project/createProject', data: this.theProject }).
  	 	      	success(function (data, status, headers, config) {
  	                     $log.info("Call Create Project Successful"); 
  	                     $scope.projectSelected=true;
  	               $scope.projectSaved=true;
  	               //this.theEvent='';
  	                  	$cookieStore.put('theProject',data);
  	                 	$scope.theProject=data;
  	             $scope.theProjectMessage='Realisation enregistree avec succes';
  	                     $log.info($scope);
  	                     
  	                     
  	            }).error(function (data, status, headers, config) {
  	                     $log.info("Call Create Project Failed");
  	                     $cookieStore.put('theProject','');
  	               $scope.theProjectMessage='Realisation ne peut etre enregistrer. Essayer plus tard';
  	                         $scope.theProject=null; 
  	                   $scope.projectSaved=false;
  	                   $scope.projectSelected=false;
  	                         $log.info($scope);
  	                   
  	                });
  	   
  	           };
  	           /**
  	 	       * End Create Project
  	 	       */
  	        
  	     /**
     * Start select Project
     * 
     */
          $scope.selectProject = function(aProject) {
        	  
        	  $scope.theProject = aProject;
        	  
        	  
        	  $scope.projectSelected=true;
              $log.info($scope.theProject.status); 
             	  	              
          };
	 /**
	  * End select Project
	  */
  	        
  	  	 /**
     * Start clear Project
     * 
     */
          $scope.clearProject = function() {
            	 
        	  $scope.theProject = '';  	  	   	  	  	                    	  
        	  $scope.projectSelected = false;
              $log.info($scope); 
             	  	              
          };
	 /**
	  * End clear Project
	  */
                   
 	  	  	                      
/**
 * Start get Projects
 * Get the list of Projects
 */
  $scope.getAllProjects = function() {
        
       $http({ method: 'POST', url: 'http://www.arelbou.com/service/project/getAllProjects', data: null }).
       success(function (data, status, headers, config) {
                $log.info("Call get All Projects Successful"); 
            	$scope.projects = data;
                $log.info($scope.projects);
             //$cookieStore.put('projects',data);
                
       }).error(function (data, status, headers, config) {
                $log.info("Call get All Project Failed");
                $log.info($scope);
             //$cookieStore.put('projects',null);
       });

  };
 /**
  * End get all Projects
  */
  
/**
    * Start get Project Album Photo
    * Get the list photo of Project
    */
         $scope.getProjectAlbum = function(aProject) {
 	          
              $http({ method: 'POST', url: 'http://www.arelbou.com/service/project/getProjectAlbum', data: aProject }).
              success(function (data, status, headers, config) {
                       $log.info("Call getProjectAlbum Successful"); 
                   	$scope.projectPictures=data;
                   	
                   	
                   
                       $log.info($scope);
                       $scope.theProject=aProject;  
                       $cookieStore.put(
								'theProject',
								aProject);
              }).error(function (data, status, headers, config) {
                       $log.info("Call getProjectAlbum Failed");
                       $log.info($scope);
              });
 
         };
 	      /**
	  * End get all Events album photos
	  */
               /**
           * Start get Album Photo for project
           * Get the list photo of Projects
           */
        $scope.getAllProjectsWithAlbum = function() {
            
            $http({ method: 'POST', url: 'http://www.arelbou.com/service/project/getAllProjectsWithAlbum', data: null }).
            success(function (data, status, headers, config) {
                     $log.info("Call getAllProjectsWithAlbum Successful"); 
                  $scope.projectsWithAlbum=data;
                    
            }).error(function (data, status, headers, config) {
                
                     $log.info("Call getAllProjectsWithAlbum Failed");
                     $log.info($scope);
            });

       };
               
 	 /**
 	  * End get all Events album photos or report
 	  */
       var url = $location.url();
		$log.info('URL='+url);
		
		if(url=='/pages/projectsEndUser'||url=='/pages/projects'){
			$scope.getAllProjects();
			$cookieStore.put('projectAlbum_reload', null);
		} else if(url=='/pages/projectAlbum' && ($scope.projectPictures==null||$scope.projectPictures=='')){
			if ($cookieStore.get('projectAlbum_reload'))
				$scope.getProjectAlbum($cookieStore.get('theProject'));	
			if (!$cookieStore.get('projectAlbum_reload')) {
				$cookieStore.put('projectAlbum_reload', "true");
				window.location.reload();
			}
		}
           	  	  	                  
}])

})();