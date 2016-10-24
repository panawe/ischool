(function() {
	'use strict';

	angular
			.module('app')
			.controller(
					'eventCtrl',
					[
							'$scope',
							'$state',
							'$window',
							'$cookieStore',
							'$log',
							'$http',
							'FileUploader',
							'$location',
							 '$interval', '$sce',
							'moment',
							function($scope, $state, $window, $cookieStore,
									$log, $http, FileUploader, $location, $interval, $sce,
									moment) {

								$scope.theEvent = $cookieStore.get('theEvent');
								$scope.theUser = $cookieStore.get('theUser');
								$scope.eventSelected = false;
								$scope.eventSaved = false;
								$scope.eventSaveSubmitted = false;
								$scope.submitted = false;
								
								$scope.chunk = function(arr, size) {
									 var newArr = [];
									  for (var i=0; i<arr.length; i+=size) {
									    newArr.push(arr.slice(i, i+size));
									  }
									 return newArr;
								}
								
								/**
								 * Delay
								 */
								
								$interval(function(){ 
									$scope.ready=true;
								},1000);
											
								/**
								 * 
								 */
								$scope.getIframeSrc = function (videoId) {
									  return $sce.trustAsResourceUrl('https://www.youtube.com/embed/' + videoId);
								};
								
								/**
								 * Start File uploader
								 */

								var uploader = $scope.uploader = new FileUploader(
										{
											url : 'http://www.arelbou.com/service/event/receiveFile'
										});

								// FILTERS

								uploader.filters.push({
									name : 'customFilter',
									fn : function(
											item /* {File|FileLikeObject} */,
											options) {
										return this.queue.length < 1000;
									}
								});

								// CALLBACKS

								uploader.onWhenAddingFileFailed = function(
										item /* {File|FileLikeObject} */,
										filter, options) {
									console.info('onWhenAddingFileFailed',
											item, filter, options);
								};
								uploader.onAfterAddingFile = function(fileItem) {
									console.info('onAfterAddingFile', fileItem);
								};
								uploader.onAfterAddingAll = function(
										addedFileItems) {
									console.info('onAfterAddingAll',
											addedFileItems);

								};
								uploader.onBeforeUploadItem = function(item) {
									console.info('onBeforeUploadItem', item);
									// item._file.name=$scope.theEvent.id+'|'+item._file.name;
									item.formData.push({
										eventId : $scope.theEvent.id
									});
									$log.info(item);

								};
								uploader.onProgressItem = function(fileItem,
										progress) {
									console.info('onProgressItem', fileItem,
											progress);
								};
								uploader.onProgressAll = function(progress) {
									console.info('onProgressAll', progress);
								};
								uploader.onSuccessItem = function(fileItem,
										response, status, headers) {
									console.info('onSuccessItem', fileItem,
											response, status, headers);
								};
								uploader.onErrorItem = function(fileItem,
										response, status, headers) {
									console.info('onErrorItem', fileItem,
											response, status, headers);
								};
								uploader.onCancelItem = function(fileItem,
										response, status, headers) {
									console.info('onCancelItem', fileItem,
											response, status, headers);
								};
								uploader.onCompleteItem = function(fileItem,
										response, status, headers) {
									console.info('onCompleteItem', fileItem,
											response, status, headers);
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
									$scope.submitted = true;
									$scope.theMessage = '';
									var email = {
										body : $scope.theEvent.report,
										subject : 'Raport de Reunion: '
												+ $scope.theEvent.title,
										sender : $scope.theUser,
										eventId : $scope.theEvent.id
									};
									if (this.emailBody == ''
											|| this.emailSubject == '') {
										$scope.theMessage = 'Le Sujet et le message sont obligatoires';
										$scope.mailSent = false;
									} else {

										$http(
												{
													method : 'POST',
													url : 'http://www.arelbou.com/service/user/saveReportAndMail',
													data : email
												})
												.success(
														function(data, status,
																headers, config) {
															$log
																	.info("Call Successful");
															$scope.email = null;
															if (data == 'Success') {
																$scope.mailSent = true;
																// $scope.emailBody='';
																// $scope.emailSubject='';
																$scope.theMessage = 'Votre annonce a ete envoye avec success';
															} else {
																$scope.mailSent = false;
																$scope.theMessage = 'Votre annonce ne peut etre envoyer en ce moment. Reessayer plus tard';
															}

															$log.info($scope);
														})
												.error(
														function(data, status,
																headers, config) {
															$log
																	.info("Call Failed");
															$log.info($scope);
															$log
																	.info($scope.email);
															$scope.mailSent = false;
															$scope.theMessage = 'Votre annonce ne peut etre envoyer en ce moment. Reessayer plus tard';
														});
									}
								};
								/**
								 * End saveReportAndMail
								 */
							  	   /**
		  	  	                 * Start get Events with Album
		  	  	                 * Get the list of Events
		  	  	                 */
		  	  	                      $scope.getTop5EventsWithAlbum = function() {
		  	  	                            
		  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getTop5EventsWithAlbum', data: null }).
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
		  	  	                 * Start get Events with Album
		  	  	                 * Get the list of Events
		  	  	                 */
		  	  	                      $scope.getAllEventsWithAlbum = function() {
		  	  	                            
		  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getAllEventsWithAlbum', data: null }).
		  	  	                           success(function (data, status, headers, config) {
		  	  	                                    $log.info("Call get All Events with album Successful"); 
		  	  	                                	$scope.eventsWithAlbumArray= $scope.chunk(data, 4); 
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
								  	   /**
				  	  	                 * Start get Events with Video
				  	  	                 * Get the list of Events
				  	  	                 */
				  	  	                      $scope.getAllEventsWithVideo = function() {
				  	  	                            
				  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getAllEventsWithVideo', data: null }).
				  	  	                           success(function (data, status, headers, config) {
				  	  	                                    $log.info("Call get All Events with Video Successful"); 
				  	  	                                    $scope.eventsWithVideoArray= $scope.chunk(data, 4); 
				  	  	                                    $log.info($scope);
				  	  	                                    
				  	  	                           }).error(function (data, status, headers, config) {
				  	  	                                    $log.info("Call get All Event with Video Failed");
				  	  	                                    $log.info($scope);
				  	  	                           });
				  	  	              
				  	  	                      };
								  	   /**
				  	  	                 * Start get Events with Video
				  	  	                 * Get the list of Events
				  	  	                 */
				  	  	                      $scope.getTop5EventsWithVideo = function() {
				  	  	                            
				  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getTop5EventsWithVideo', data: null }).
				  	  	                           success(function (data, status, headers, config) {
				  	  	                                    $log.info("Call get All Events with Video Successful"); 
				  	  	                                	$scope.eventsWithVideo=data; 
				  	  	                                    $log.info($scope);
				  	  	                                    
				  	  	                           }).error(function (data, status, headers, config) {
				  	  	                                    $log.info("Call get All Event with Video Failed");
				  	  	                                    $log.info($scope);
				  	  	                           });
				  	  	              
				  	  	                      };
				  	  	            	 /**
				  	  	            	  * End get all Events with Video
				  	  	            	  */
								/**
								 * Begin Event Calendar
								 */

								var vm = this;

								// These variables MUST be set as a minimum for
								// the calendar to work
								vm.calendarView = 'year';
								vm.viewDate = new Date();
								vm.isCellOpen = true;

								vm.eventClicked = function(event) {
									// alert.show('Clicked', event);
									$scope.currEvent = event;
									$('#myModalLabel').text(event.title);
									$('#myModal').modal('show');
								};

								vm.eventEdited = function(event) {
									// alert.show('Edited', event);
								};

								vm.eventDeleted = function(event) {
									// alert.show('Deleted', event);
								};

								vm.eventTimesChanged = function(event) {
									// alert.show('Dropped or resized', event);
								};

								vm.toggle = function($event, field, event) {
									$event.preventDefault();
									$event.stopPropagation();
									event[field] = !event[field];
								};

								$scope.vm = vm;

								/**
								 * End Event Calendar
								 */
								
								/**
								 * Start select Event
								 * 
								 */
								$scope.selectEvent = function(aEvent) {

									$scope.theEvent = aEvent;

									$scope.eventSelected = true;
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

									$scope.theEvent = '';
									$scope.eventSelected = false;
									$log.info($scope);

								};
								/**
								 * End clear Event
								 */

								/**
								 * Start get Events Get the list of Events
								 */
								$scope.getAllEvents = function() {

									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/event/getAllEvents',
												data : null
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call get All Events Successful");
														$scope.events = data;
														$log.info($scope);
														$log.info($scope.events);
														// $cookieStore.put('events',data);

													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call get All Event Failed");
														$log.info($scope);
														// $cookieStore.put('events',null);
													});

								};
								/**
								 * End get all Events
								 */

								/**
								 * Start create Event
								 */
								$scope.createEvent = function() {
									$scope.eventSaveSubmitted = true;
									// $scope.theEvent.beginDateTime =
									// $scope.beginEndDateTime.split("-")[0];
									// $scope.theEvent.endDateTime =
									// $scope.beginEndDateTime.split("-")[1];
									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/event/createEvent',
												data : this.theEvent
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call Create event Successful");
														$scope.eventSelected = true;
														$scope.eventSaved = true;
														// this.theEvent='';
														$cookieStore.put(
																'theEvent',
																data);
														$scope.theEvent = data;
														$scope.theEventMessage = 'Evenement enregistre avec succes';
														$log.info($scope);

													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call Create Event Failed");
														$cookieStore.put(
																'theEvent', '');
														$scope.theEventMessage = 'Evenement ne peut etre enregistrer. Essayer plus tard';
														$scope.theEvent = null;
														$scope.eventSaved = false;
														$scope.eventSelected = false;
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
									aEvent.deleted = false;
									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/event/deleteEvent',
												data : aEvent
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call deleteEvent Successful");
														aEvent.deleted = true;
														$log.info($scope);
													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call deleteEvent Failed");
														$log.info($scope);

													});

								};
								/**
								 * End Delete
								 */

								/**
								 * Start get Album Photo Get the list photo of
								 * Events
								 */
								$scope.getEventAlbum = function(aEvent) {
									//report too big for cookies
									if(aEvent!=null){
										aEvent.report=null;
									}
									$cookieStore.remove("theEvent");
									$cookieStore.put('theEvent',aEvent);
									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/event/getEventAlbum',
												data : aEvent
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call get All Events photo Successful");
														$scope.EventPictures = data;
														$log.info($scope);
														$scope.theEvent = aEvent;												

													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call get All Event phot Failed");
														$log.info($scope);
													});

								};
								/**
								 * End get all Events album photos
								 */
								
						        /* Start get Album Photo or Report
	  	  	  	                 * Get the list photo of Events
	  	  	  	                 */
	  	  	  	                      $scope.getAllEventsWithAlbumOrRepport = function() {
 	  	  	                            
	  	  	  	                           $http({ method: 'POST', url: 'http://www.arelbou.com/service/event/getAllEventsWithRepport', data: null }).
	  	  	  	                           success(function (data, status, headers, config) {
	  	  	  	                                    $log.info("Call get All Events with album Successful"); 
	  	  	  	                                	$scope.eventsWithAlbumReport=data;
	  	  	  	                                   
	  	  	  	                           }).error(function (data, status, headers, config) {
	  	  	  	                        	   		 
	  	  	  	                                    $log.info("Call get All Event with album Failed");
	  	  	  	                                    $log.info($scope);
	  	  	  	                           });
	  	  	  	              
	  	  	  	                      };
	  	  	  	            	 /**
	  	  	  	            	  * End get all Events album photos or report
	  	  	  	            	  */
	  	  	  	                      
								
								var url = $location.url();
								$log.info('URL='+url);
								
								if(url=='/pages/photos'){
									$scope.getAllEventsWithAlbum();
									$cookieStore.remove("eventAlbum_reload");
								}if(url=='/pages/videos'){
									$scope.getAllEventsWithVideo();
									$cookieStore.remove("eventAlbum_reload");
								}if(url=='/pages/main'){
									
									$scope.getTop5EventsWithAlbum();
									$scope.getTop5EventsWithVideo();
									$cookieStore.remove("eventAlbum_reload");
								}else if(url=='/pages/eventAlbum' &&  ($scope.EventPictures==null||$scope.EventPictures=='')){								
									if ($cookieStore.get('eventAlbum_reload'))
										$scope.getEventAlbum($cookieStore.get('theEvent'));		
											
									
									if (!$cookieStore.get('eventAlbum_reload')) {
										$cookieStore.put('eventAlbum_reload', "true");
										window.location.reload();
									}
								}else if(url=='/pages/eventCalendar'||url=='/pages/events'){
									$scope.getAllEvents();
								}else if(url=='/pages/meetings'){
									$scope.getAllEventsWithAlbumOrRepport();
									$cookieStore.remove("eventAlbum_reload");									
								}
								

							} ])

})();