(function () {
    'use strict';
    
    angular.module('app')
                   .controller('mainCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
            function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {

                	   /**
  	  	                 * Start get Events with Album
  	  	                 * Get the list of Events
  	  	                 */
  	  	                      $scope.getAllEventsWithAlbum = function() {
  	  	                            
  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getAllEventsWithAlbum', data: null }).
  	  	                           success(function (data, status, headers, config) {
  	  	                                    $log.info("Call get All Events with album Successful"); 
  	  	                                	$scope.eventsWithAlbum=data;
  	  	                                    $log.info($scope);
  	                                     //$cookieStore.put('eventsWithAlbum',data);
  	  	                                    
  	  	                           }).error(function (data, status, headers, config) {
  	  	                                    $log.info("Call get All Event with album Failed");
  	  	                                    $log.info($scope);
  	                                     //$cookieStore.put('eventsWithAlbum',null);
  	  	                           });
  	  	              
  	  	                      };
  	  	            	 /**
  	  	            	  * End get all Events with Album
  	  	            	  */
		         	
	  	  	                  $scope.getAllEventsWithAlbum();
}])


.controller('userCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {

	$scope.$state = $state;
    $scope.theUser=$cookieStore.get('theUser');
    
    /**
     * Start log on function
     * Get userName and Password and return pass or failed login
     * If log in sucessful, put user in session cookie and use it on the UI.
     */
          $scope.login = function() {
              //$log.info("login successful");
              //$log.info("UserName="+$scope.userName+", password="+$scope.password);
              var User={"userName":$scope.userName, "password":$scope.password};
                
               $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/login', data: User }).
               success(function (data, status, headers, config) {
                        $log.info("Call Successful"); 
                        if(data!=null&&data!=''){
                        	$scope.failedLogin=false;
                        	
                        }else{
                        	$scope.failedLogin=true;
                        }

                    	$cookieStore.put('theUser',data);
                    	$scope.theUser=data;
                        $log.info($scope);
                        
               }).error(function (data, status, headers, config) {
                        $log.info("Call Failed");
                        $cookieStore.put('theUser','');
                        $scope.theUser=null;
                        $scope.failedLogin=true;
                        $log.info($scope);
               });
  
          };
	 /**
	  * End Log in
	  */
	                      
      
          /**
           * Start Logout
           */
    $scope.logout=function(){
        $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/logout' }).
        success(function (data, status, headers, config) {
        	$log.info("Call Successful"); 
            $cookieStore.put('theUser',data);
            $scope.theUser=data;
            $log.info($scope);
                 
        }).error(function (data, status, headers, config) {
            $log.info("Call Failed");
        });
    	
        $cookieStore.put('theUser','');
        $scope.theUser='';
        $log.info($scope);
    };
    /**
     * End Log out
     */
    
    /**
     * Start create User
     */
    $scope.createUser = function() {  
    	
         $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/createUser', data: $scope.newUser }).
         success(function (data, status, headers, config) {
                  $log.info("Call Successful"); 
                  if(data!=null&&data!=''){
                  	$scope.failedLogin=false;
                  	
                  }else{
                  	$scope.failedLogin=true;
                  }

              	$cookieStore.put('theUser',data);
              	$scope.theUser=data;
                  $log.info($scope);
                  
         }).error(function (data, status, headers, config) {
                  $log.info("Call Failed");
                  $cookieStore.put('theUser','');
                  $scope.theUser=null;
                  $scope.failedLogin=true;
                  $log.info($scope);
         });

    };
    /**
     * End Create users
     */
    
	
		                /**
     * Save Profile
     */
    $scope.saveUser = function() {  
    	$scope.saveUserSubmitted=true;
         $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/saveUser', data: $scope.theUser }).
         success(function (data, status, headers, config) {
                  $log.info("Call Successful"); 
                  if(data!=null&&data!=''){
                  	$scope.failedLogin=false;
                  	
                  }else{
                  	$scope.failedLogin=true;
                  }

              	$cookieStore.put('theUser',data);
              	$scope.theUser=data;
                  $log.info($scope);
                $scope.theUserMessage='Sauvegarde avec succes';
                  
         }).error(function (data, status, headers, config) {
                  $log.info("Call Failed");
                  $cookieStore.put('theUser','');
                  $scope.theUser=null;
                  $scope.failedLogin=true;
                  $log.info($scope);
                  $scope.theUserMessage='Sauvegarde Echouee. Reessayer plus tard';
         });

    };
    
    $scope.resetUserMessage= function(){
    	$scope.saveUserSubmitted=false;
    	$scope.theUserMessage='';
    }
    /**
     * End save user profile
     */             
    
    /**
     * Start get All users
     * Get the list of members
     */
          $scope.getAllUsers = function() {
                
               $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/getAllMembers', data: null }).
               success(function (data, status, headers, config) {
                        $log.info("Call Successful"); 
                    	$scope.users=data;
                        $log.info($scope);
               }).error(function (data, status, headers, config) {
                        $log.info("Call Failed");
                        $log.info($scope);
               });
  
          };
	 /**
	  * End get all users
	  */
                /**
                 * Begin Find a user
                 * 
                 */
                      $scope.findMembers = function() {
                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/findMembers', data:{searchText:$scope.searchText}}).
                           success(function (data, status, headers, config) {
                                    
                                $log.info("Call find Members Successful"); 
                        	$scope.searchResult=data;
                            $log.info($scope);
                            //$cookieStore.put('searchResult',data);
                            $location.path('pages.searchResults');
                            $state.go('pages.searchResults');
                                    
                           }).error(function (data, status, headers, config) {
                                    $log.info("Call Find Members Failed");
                                    $scope.searchResult='';
                                    //$cookieStore.put('searchResult','');
                           });
              
                      };
            	 /**
            	  * End Find a user
            	  */

                      
                /**
                 * Start approve Member
                 * 
                 */
                      $scope.approveMember = function(aUser) {
                            
                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/approveMember', data: aUser}).
                           success(function (data, status, headers, config) {
                                    
                                $log.info("Call Successful"); 
                                aUser.status=1;
                                $log.info($scope); 
                           }).error(function (data, status, headers, config) {
                                $log.info("Call Failed");
                                $log.info($scope);
                           });
              
                      };
            	 /**
            	  * End get approve Member
            	  */

  	                /**
  	                 * Start Reject Member
  	                 * 
  	                 */
  	                      $scope.rejectMember = function(aUser) {
  	                            
  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/rejectMember', data: aUser}).
  	                           success(function (data, status, headers, config) {
  	                                    
  	                                $log.info("Call Successful"); 
  	                                aUser.status=2;
  	                                $log.info($scope); 
  	                           }).error(function (data, status, headers, config) {
  	                                $log.info("Call Failed");
  	                                $log.info($scope);
  	                           });
  	              
  	                      };
  	            	 /**
  	            	  * End get Pending users
  	            	  */

  	                    /**
	  	  	                 * Start get Pending users
	  	  	                 * Get the list of members
	  	  	                 */
	  	  	                      $scope.getPendingMembers = function() {
	  	  	                            
	  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/getPendingMembers', data: $scope.mailContent}).
	  	  	                           success(function (data, status, headers, config) {
	  	  	                                    
	  	  	                                $log.info("Call Successful"); 
		                                	$scope.pendingUsers=data;
		                                    $log.info($scope);
	                                  // $cookieStore.put('pendingMembers',data);
	  	  	                                    
	  	  	                           }).error(function (data, status, headers, config) {
	  	  	                                    $log.info("Call Failed");
	  	  	                           });
	  	  	              
	  	  	                      };
	  	  	            	 /**
	  	  	            	  * End get Pending users
	  	  	            	  */
	  	  	                      
	  	  	  	  	  	        
	  	  		  	  	  	  	 /**
	  	  	  	  	  	                 * Start select Event
	  	  	  	  	  	                 * 
	  	  	  	  	  	                 */
	  	  	  	  	  	                      $scope.selectEvent = function(aEvent) {
	  	  	   	  	  	                    	  
	  	  	  	  	  	                    	  $scope.theEvent=aEvent;
	  	  	  	  	  	                    	  
	  	  	  	  	  	                    	  $scope.eventSelected=true;
	  	  	  	  	  	                          $log.info($scope); 
	  	  	  	  	  	                         	  	              
	  	  	  	  	  	                      };
	  	  	  	  	  	            	 /**
	  	  	  	  	  	            	  * End select Event
	  	  	  	  	  	            	  */
	  	  	  		  	  	  	  	        
	  	  	  	  	 	  	  	  	  	 /**
	  	  	  	  	   	  	  	                 * Start clear Event
	  	  	  	  	   	  	  	                 * 
	  	  	  	  	   	  	  	                 */
	  	  	  	  	   	  	  	                      $scope.clearEvent = function() {
	  	  	  	  	    	  	  	                    	 
	  	  	  	  	   	  	  	                    	  $scope.theEvent='';  	  	   	  	  	                    	  
	  	  	  	  	   	  	  	                    	  $scope.eventSelected=false;
	  	  	  	  	   	  	  	                          $log.info($scope); 
	  	  	  	  	   	  	  	                         	  	              
	  	  	  	  	   	  	  	                      };
	  	  	  	  	   	  	  	            	 /**
	  	  	  	  	   	  	  	            	  * End clear Event
	  	  	  	  	   	  	  	            	  */
	  	  	 	  	  	                      
	  	  	  	  	  	                /**
	  	  	  	  	  	                 * Start get Events
	  	  	  	  	  	                 * Get the list of Events
	  	  	  	  	  	                 */
	  	  	  	  	  	                      $scope.getAllEvents = function() {
	  	  	  	  	  	                            
	  	  	  	  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getAllEvents', data: null }).
	  	  	  	  	  	                           success(function (data, status, headers, config) {
	  	  	  	  	  	                                    $log.info("Call get All Events Successful"); 
	  	  	  	  	  	                                	$scope.events=data;
	  	  	  	  	  	                                    $log.info($scope);
	  	  	  	  	  	                                    $log.info($scope.events);
	  	  	  	  	                                     //$cookieStore.put('events',data);
	  	  	  	  	  	                                    
	  	  	  	  	  	                           }).error(function (data, status, headers, config) {
	  	  	  	  	  	                                    $log.info("Call get All Event Failed");
	  	  	  	  	  	                                    $log.info($scope);
	  	  	  	  	                                     //$cookieStore.put('events',null);
	  	  	  	  	  	                           });
	  	  	  	  	  	              
	  	  	  	  	  	                      };
	  	  	  	  	  	            	 /**
	  	  	  	  	  	            	  * End get all Events
	  	  	  	  	  	            	  */
	  	  	  	  	  	              
	  	  	  	  	  	  	                      
	  	  	  	  	  	  	            
	  	  	  	  	  	                /**
	  	  	  	  	  	                 * Start create Event
	  	  	  	  	  	                 */
	  	  	  	  	  	                $scope.createEvent = function() {  
	  	  	  	  	  	                	$scope.eventSaveSubmitted=true;
	  	  	  	  	  	                	//$scope.theEvent.beginDateTime = $scope.beginEndDateTime.split("-")[0];
	  	  	  	  	  	                	//$scope.theEvent.endDateTime = $scope.beginEndDateTime.split("-")[1];
	  	  	  	  	  	                     $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/createEvent', data: this.theEvent }).
	  	  	  	  	  	                     success(function (data, status, headers, config) {
	  	  	  	  	  	                              $log.info("Call Create event Successful"); 
	  	  	  	  	  	                              $scope.eventSelected=true;
	  	  	  	  	  	                        $scope.eventSaved=true;
	  	  	  	  	  	                        //this.theEvent='';
	  	  	   	  	  	                          	$cookieStore.put('theEvent',data);
	  	  	  	  	  	                          	$scope.theEvent=data;
	  	  	  	  	  	                      $scope.theEventMessage='Evenement enregistre avec succes';
	  	  	  	  	  	                              $log.info($scope);
	  	  	  	  	  	                              
	  	  	  	  	  	                              
	  	  	  	  	  	                     }).error(function (data, status, headers, config) {
	  	  	  	  	  	                              $log.info("Call Create Event Failed");
	  	  	  	  	  	                              $cookieStore.put('theEvent','');
	  	  	  	  	  	                        $scope.theEventMessage='Evenement ne peut etre enregistrer. Essayer plus tard';
	  	  	  	  	  	                              $scope.theEvent=null; 
	  	  	  	  	  	                        $scope.eventSaved=false;
	  	  	  	  	  	                        $scope.eventSelected=false;
	  	  	  	  	  	                              $log.info($scope);
	  	  	  	  	  	                        
	  	  	  	  	  	                     });
	  	  	  	  	  	        
	  	  	  	  	  	                };
	  	  	  	  	  	                /**
	  	  	  	  	  	                 * End Create Event
	  	  	  	  	  	                 */
	  	  	  	  	  	                /**
	  	  	  	  	  	                 * Start Delete Event
	  	  	  	  	  	                 * 
	  	  	  	  	  	                 */
	  	  	  	  	  	                      $scope.deleteEvent = function(aEvent) {
	  	  	  	  	  	                    	  aEvent.deleted=false;
	  	  	  	  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/deleteEvent', data: aEvent}).
	  	  	  	  	  	                           success(function (data, status, headers, config) {  	  	  	                                    
	  	  	  	  	  	                                $log.info("Call deleteEvent Successful"); 
	  	  	  	  	  	                                aEvent.deleted=true;
	  	  	  	  	  	                                $log.info($scope); 
	  	  	  	  	  	                           }).error(function (data, status, headers, config) {
	  	  	  	  	  	                                $log.info("Call deleteEvent Failed");
	  	  	  	  	  	                                $log.info($scope);
	  	  	  	  	  	                                
	  	  	  	  	  	                           });
	  	  	  	  	  	              
	  	  	  	  	  	                      };
	  	  	  	  	  	            	 /**
	  	  	  	  	  	            	  * End Delete 
	  	  	  	  	  	            	  */
	  	  	  	  	  	                      
	  	  	  	  	  	                /**
	  	  	  	  	  	  	                 * Start get Album Photo
	  	  	  	  	  	  	                 * Get the list photo of Events
	  	  	  	  	  	  	                 */
	  	  	  	  	  	  	                      $scope.getEventAlbum = function(aEvent) {
	  	  	  	  	  	  	                            
	  	  	  	  	  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getEventAlbum', data: aEvent }).
	  	  	  	  	  	  	                           success(function (data, status, headers, config) {
	  	  	  	  	  	  	                                    $log.info("Call get All Events photo Successful"); 
	  	  	  	  	  	  	                                	$scope.EventPictures=data;
	  	  	  	  	  	  	                                    $log.info($scope);
	  	  	  	  	  	  	                                    $scope.theEvent=aEvent;
	  	  	  	  	  	  	                                    
	  	  	  	  	  	  	                           }).error(function (data, status, headers, config) {
	  	  	  	  	  	  	                                    $log.info("Call get All Event phot Failed");
	  	  	  	  	  	  	                                    $log.info($scope);
	  	  	  	  	  	  	                           });
	  	  	  	  	  	  	              
	  	  	  	  	  	  	                      };
	  	  	  	  	  	  	            	 /**
	  	  	  	  	  	  	            	  * End get all Events album photos
	  	  	  	  	  	  	            	  */
	  	  	  	  	  	  	                      	
	  	  	  		  	  	  	  	        
	  	  	  	  		  	  	  	  	   /**
	  	  	  	  	  	  	  	  	  	           * Begin Show Modal
	  	  	  	  	  	  	  	  	  	           */  	  	  	  	                      
	  	  	  	  	  	  	  	  	          $scope.showUserModal = function(user){
	  	  	  	  	  	  	  	  	        	    $scope.currUser = user;
	  	  	  	  	  	  	  	  	        	  $('#myModalLabel').text(user.firstName
	  	  	  	  	  	  	  	  	               + ' ' + user.lastName);
	  	  	  	  	  	  	  	  	        	  $('#myModal').modal('show');
	  	  	  	  	  	  	  	  	          }
	  	  	  	  	  	  	  	  	      /**
	  	  	  	  	  	  	  	  	  	   * End Show Modal
	  	  	  	  	  	  	  	  	  	   */
	  	  	  	  	  	  	  	  	          

              	  	  	                  
}])



.controller('eventCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {
	
    $scope.theEvent=$cookieStore.get('theEvent'); 
    $scope.eventSelected=false;
    $scope.eventSaved=false;
    $scope.eventSaveSubmitted=false;
    $scope.submitted=false;
            /**
             * Start File uploader
             */
            
var uploader = $scope.uploader = new FileUploader({
  url: 'http://www.arelbou.com/service/event/receiveFile'
});


// FILTERS

uploader.filters.push({
  name: 'customFilter',
  fn: function(item /*{File|FileLikeObject}*/, options) {
      return this.queue.length < 1000;
  }
});

// CALLBACKS

uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
  console.info('onWhenAddingFileFailed', item, filter, options);
};
uploader.onAfterAddingFile = function(fileItem) {
  console.info('onAfterAddingFile', fileItem);
};
uploader.onAfterAddingAll = function(addedFileItems) {
  console.info('onAfterAddingAll', addedFileItems);
  
};
uploader.onBeforeUploadItem = function(item) {
  console.info('onBeforeUploadItem', item);
 // item._file.name=$scope.theEvent.id+'|'+item._file.name;
  item.formData.push({eventId: $scope.theEvent.id});
  $log.info(item);
  
};
uploader.onProgressItem = function(fileItem, progress) {
  console.info('onProgressItem', fileItem, progress);
};
uploader.onProgressAll = function(progress) {
  console.info('onProgressAll', progress);
};
uploader.onSuccessItem = function(fileItem, response, status, headers) {
  console.info('onSuccessItem', fileItem, response, status, headers);
};
uploader.onErrorItem = function(fileItem, response, status, headers) {
  console.info('onErrorItem', fileItem, response, status, headers);
};
uploader.onCancelItem = function(fileItem, response, status, headers) {
  console.info('onCancelItem', fileItem, response, status, headers);
};
uploader.onCompleteItem = function(fileItem, response, status, headers) {
  console.info('onCompleteItem', fileItem, response, status, headers);
};
uploader.onCompleteAll = function() {
  console.info('onCompleteAll');
};

console.info('uploader', uploader);
            
            
            /**
             * End File Uploader
             */
/**
 * Start saveReportAndMail
 *  
 */
      $scope.saveReportAndMail = function() { 
      $scope.submitted=true;
    $scope.theMessage='';
       var email={body:$scope.theEvent.report, 
			  subject:'Raport de Reunion: '+$scope.theEvent.title,
			  sender:$scope.theUser,
			  eventId:$scope.theEvent.id};
       if(this.emailBody=='' || this.emailSubject==''){
    	 $scope.theMessage='Le Sujet et le message sont obligatoires';
    		 $scope.mailSent=false;
       } else{

           $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/saveReportAndMail', data: email}).
           success(function (data, status, headers, config) {
                    $log.info("Call Successful"); 
                  $scope.email=null;
                  if(data=='Success'){
                	 $scope.mailSent=true;
                	// $scope.emailBody='';
                     //$scope.emailSubject='';
                     $scope.theMessage='Votre annonce a ete envoye avec success';
                  }else{
                	 $scope.mailSent=false;
                	$scope.theMessage='Votre annonce ne peut etre envoyer en ce moment. Reessayer plus tard';
                  }
                 
               
                  $log.info($scope);
           }).error(function (data, status, headers, config) {
                    $log.info("Call Failed");
                    $log.info($scope);
                    $log.info($scope.email);
                  $scope.mailSent=false;
                $scope.theMessage='Votre annonce ne peut etre envoyer en ce moment. Reessayer plus tard';
           });
       }
      };
 /**
  * End saveReportAndMail
  */    
  	      /**
	  	       * Begin Event Calendar
	  	       */
  	     
	  	  var vm = this;

  //These variables MUST be set as a minimum for the calendar to work
  vm.calendarView = 'year';
  vm.viewDate = new Date();
  vm.isCellOpen = true;

  vm.eventClicked = function(event) {
    //alert.show('Clicked', event);
	 $scope.currEvent = event;
   	  $('#myModalLabel').text(event.title);
   	  $('#myModal').modal('show');
  };

  vm.eventEdited = function(event) {
    //alert.show('Edited', event);
  };

  vm.eventDeleted = function(event) {
    //alert.show('Deleted', event);
  };

  vm.eventTimesChanged = function(event) {
   // alert.show('Dropped or resized', event);
  };

  vm.toggle = function($event, field, event) {
    $event.preventDefault();
    $event.stopPropagation();
    event[field] = !event[field];
  };
	  	         
  $scope.vm=vm;
	  	          
	  	          /**
	  	           * End Event Calendar
	  	           */

           	  	  	                  
}])


.controller('projectCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {

	        
	  	  
  	 	   /**
  	        * Start create Project
  	        */
  	 	    
  	 	    $scope.projectSelected=false;
  	         $scope.projectSaved=false;
  	         $scope.projectSaveSubmitted=false;
  	         
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
                       $log.info("Call get All Projects photo Successful"); 
                   	$scope.ProjectPictures=data;
                       $log.info($scope);
                       $scope.theProject=aProject;  	                                    
              }).error(function (data, status, headers, config) {
                       $log.info("Call get All Project phot Failed");
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
                     $log.info("Call get All Projects with album Successful"); 
                  $scope.projectsWithAlbum=data;
                    
            }).error(function (data, status, headers, config) {
                
                     $log.info("Call get All Projects with album Failed");
                     $log.info($scope);
            });

       };
               
 	 /**
 	  * End get all Events album photos or report
 	  */
       
           	  	  	                  
}])

.controller('utilCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {
	

    $scope.submitted=false;

           
            //function to open links in new tabs
            $scope.openInNewTab = function(link){
                    $window.open(link, '_blank');
                };
                
           	  	  	                  
}])



.controller('paymentCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {
          
          /**
                 * Start create Payment
                 */
                $scope.makePayment = function() {  
                	var transaction={amount:this.amount,
                			comment:this.comment,
                			year:this.year,
                			month:this.month,
                			modifiedBy:$scope.theUser.id,
                			io:1,
                			rebate:0.0,
                			user:$scope.currUser,
                			paymentType: {id:this.paymentType}
                			
                	};
                	$scope.paymentSaveSubmitted=true;
                     $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/makePayment', data: transaction }).
                     success(function (data, status, headers, config) {
                     $log.info("Call makePayment Successful");  
                     $scope.paymentSaved=true; 
                     if(data=='Success'){
                    	 $scope.thePaymentMessage='Payement Effectue succes';
                    	 
                     }else{
                    	 $scope.thePaymentMessage='Le payement a echoue';
                    	 $scope.paymentSaved=false;
                     }
                    
                     $log.info($scope);
                              
                              
                     }).error(function (data, status, headers, config) {
                        $log.info("Call makePayment Failed");
                        $scope.thePaymentMessage='Le payement a echoue';
                        $scope.paymentSaved=false;
                        $log.info($scope);
                        
                     });
        
                };
                /**
                 * End Create Payment
                 */

        /**
                 * Start create Expense
                 */
                $scope.saveExpense = function() {  
                	var transaction={amount:this.amount,
                			comment:this.comment,
                			year:this.year,
                			month:this.month,
                			modifiedBy:$scope.theUser.id,
                			io:0,
                			rebate:0.0,
                			paymentType: {id:this.expenseType}
                			
                	};
                	$scope.paymentSaveSubmitted=true;
                     $http({ method: 'POST', url: 'http://www.arelbou.com/service/user/saveExpense', data: transaction }).
                     success(function (data, status, headers, config) {
                     $log.info("Call makePayment Successful");  
                     $scope.paymentSaved=true; 
                     if(data=='Success'){
                    	 $scope.thePaymentMessage='Payement Effectue succes';
                    	 
                     }else{
                    	 $scope.thePaymentMessage='Le payement a echoue';
                    	 $scope.paymentSaved=false;
                     }
                    
                     $log.info($scope);
                              
                              
                     }).error(function (data, status, headers, config) {
                        $log.info("Call makePayment Failed");
                        $scope.thePaymentMessage='Le payement a echoue';
                        $scope.paymentSaved=false;
                        $log.info($scope);
                        
                     });
        
                };
                /**
                 * End Create Expense
                 */
                
                /**
                 * Start clear Payment
                 */
                $scope.clearPayment = function() {   
                		$log.info('Clear Payment Called');
                		 $scope.paymentSaveSubmitted=false;
                    	 $scope.thePaymentMessage='';
                    	 $scope.month='';
                    	 $scope.year='';
                    	 $scope.comment='';
                    	 $scope.amount='';
                    	 $scope.paymentType='';	  	
                    	 $scope.paymentSaved=false;
                    	 $log.info($scope);
                };
                /**
                 * End clear Payment
                 */
                
                $scope.clearExpense = function() {   
            		$log.info('Clear Expense Called');
            		 $scope.paymentSaveSubmitted=false;
                	 $scope.thePaymentMessage='';
                	 $scope.month='';
                	 $scope.year='';
                	 $scope.comment='';
                	 $scope.amount='';
                	 $scope.expenseType='';	  	
                	 $scope.paymentSaved=false;
                	 $log.info($scope);
            };
            /**
             * End clear Payment
             */
        $scope.drawPayments = function() {
        	
        	$http({ method: 'POST', url: 'http://www.arelbou.com/service/user/getYearlySummary', data: null }).
   success(function (data, status, headers, config) {
            $log.info("Call get getYearlySummary"); 
   	      Morris.Bar({
	  	  	  	  element: 'bar-example',
	  	  	  	  data:data,
	  	  	  	  xkey: 'year',
	  	  	  	  ykeys: ['in', 'out'],
	  	  	  	  labels: ['Cotisations', 'Depenses']
	  	  	  	});
           
   }).error(function (data, status, headers, config) {
	   		 
            $log.info("Call get getYearlySummary Failed");
            $log.info($scope);
   });	  	  	  	  	    

        };

           	  	  	                  
}])


.controller('otherCtrl', [ '$scope', '$state','$window','$cookieStore','$log','$http','FileUploader','$location','moment', 
                          function ($scope, $state,$window, $cookieStore,$log,$http,FileUploader,$location,moment) {

           	  	  	                  
}])

})();