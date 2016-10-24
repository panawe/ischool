(function() {
	'use strict';

	angular
			.module('app')
			.controller(
					'sideBarCtrl',
					[
							'$scope',
							'$state',
							'$window',
							'$cookieStore',
							'$log',
							'$http',
							'FileUploader',
							'$location',
							'moment',
							function($scope, $state, $window, $cookieStore,
									$log, $http, FileUploader, $location,
									moment) {

								   //function to open links in new tabs
					            $scope.openInNewTab = function(link){
					                    $window.open(link, '_blank');
					                };

								/**
								 * Start get announces
								 */
								$scope.getActiveAnnounces = function() {

									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/announce/getActiveAnnounces',
												data : null
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call get Active Announces Successful");
														$scope.activeAnnounces = data;
														$log.info($scope);
													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call get Active Announces Failed");
														$log.info($scope);
													});

								};

								
								
								/**
								 * Start get Events Get the list of Events
								 */
								$scope.getFutureEvents = function() {

									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/event/getFutureEvents',
												data : null
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call get Future Events Successful");
														$scope.futureEvents = data;
														$log.info($scope);
														$log.info($scope.events);
														// $cookieStore.put('events',data);

													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call get Future Event Failed");
														$log.info($scope);
														// $cookieStore.put('events',null);
													});

								};
								
								
								/**
								 * Begin Show Modal
								 */
								$scope.showEventModal = function(event) {
									$scope.currEvent = event;
									$('#myEventModalLabel').text(event.title);
									$('#myEventModal').modal('show');
								}
								/**
								 * End show Modal
								 * 
								 * /
						
								/**
								 * Begin Show Modal
								 */
								$scope.showAnnounceModal = function(announce) {
									$scope.currAnnounce = announce;
									$('#myAnnounceModalLabel').text(announce.title);
									$('#myAnnounceModal').modal('show');
								}
								/**
								 * End get all Events
								 */

								$scope.getAdvertisements = function() {  
									
									var activeAdvertisements = {};
									$http(
										{
											method : 'POST',
											url : 'http://www.arelbou.com/service/marketing/getActiveMarketings',
											data : null
										})
									.success(
										function(data, status,
												headers, config) {
											$log
													.info("Call get Active Advertisements Successful");
											$scope.activeAdvertisements = data;
											  
											$log.info($scope);
										})
									.error(
											function(data, status,
													headers, config) {
												$log
														.info("Call get Active Advertisements Failed");
												$log.info($scope);
											});
											
									  
								}
								
								
								
								$scope.getFutureEvents();								
								$scope.getActiveAnnounces();
								$scope.getAdvertisements();
								


							} ])
})();