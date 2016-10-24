(function() {
	'use strict';

	angular
			.module('app')
			.controller(
					'paymentCtrl',
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

								$scope.paymentTypes = [ {
									id : 0,
									name : ''
								}, {
									id : 1,
									name : 'Cotisation mensuelle'
								}, {
									id : 2,
									name : 'Cotisation Annuelle'
								}, {
									id : 3,
									name : 'Don'
								} ];

								$scope.expenseTypes = [ {
									id : 0,
									name : ''
								}, {
									id : 4,
									name : 'Organisation Rencontre'
								}, {
									id : 5,
									name : 'Projet'
								}, {
									id : 6,
									name : 'Donation'
								}, {
									id : 7,
									name : 'Frais de fonctionnement'
								}, {
									id : 8,
									name : 'Autre'
								} ];

								$scope.years = [ {
									id : 0,
									name : ''
								}, {
									id : 1,
									name : 2016
								}, {
									id : 2,
									name : 2017
								}, {
									id : 3,
									name : 2018
								}, {
									id : 4,
									name : 2019
								}, {
									id : 5,
									name : 2020
								} ];

								$scope.months = [ {
									name : ''
								}, {
									name : 'Janvier'
								}, {
									name : 'Fevrier'
								}, {
									name : 'Mars'
								}, {
									name : 'Avril'
								}, {
									name : 'Mai'
								}, {
									name : 'Juin'
								}, {
									name : 'Juillet'
								}, {
									name : 'Aout'
								}, {
									name : 'Septembre'
								}, {
									name : 'October'
								}, {
									name : 'Novembre'
								}, {
									name : 'Decembre'
								} ];

								/**
								 * Start create Payment
								 */
								$scope.makePayment = function() {
									var transaction = {
										amount : this.amount,
										comment : this.comment,
										year : this.year,
										month : this.month,
										modifiedBy : $scope.theUser.id,
										io : 1,
										rebate : 0.0,
										user : $scope.currUser,
										paymentType : {
											id : this.paymentType
										}

									};
									$scope.paymentSaveSubmitted = true;
									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/user/makePayment',
												data : transaction
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call makePayment Successful");
														$scope.paymentSaved = true;
														if (data == 'Success') {
															$scope.thePaymentMessage = 'Payement Effectue succes';

														} else {
															$scope.thePaymentMessage = 'Le payement a echoue';
															$scope.paymentSaved = false;
														}

														$log.info($scope);

													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call makePayment Failed");
														$scope.thePaymentMessage = 'Le payement a echoue';
														$scope.paymentSaved = false;
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
									var transaction = {
										amount : this.amount,
										comment : this.comment,
										year : this.year,
										month : this.month,
										modifiedBy : $scope.theUser.id,
										io : 0,
										rebate : 0.0,
										paymentType : {
											id : this.expenseType
										}

									};
									$scope.paymentSaveSubmitted = true;
									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/user/saveExpense',
												data : transaction
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call makePayment Successful");
														$scope.paymentSaved = true;
														if (data == 'Success') {
															$scope.thePaymentMessage = 'Payement Effectue succes';

														} else {
															$scope.thePaymentMessage = 'Le payement a echoue';
															$scope.paymentSaved = false;
														}

														$log.info($scope);

													})
											.error(
													function(data, status,
															headers, config) {
														$log
																.info("Call makePayment Failed");
														$scope.thePaymentMessage = 'Le payement a echoue';
														$scope.paymentSaved = false;
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
									$scope.paymentSaveSubmitted = false;
									$scope.thePaymentMessage = '';
									$scope.month = '';
									$scope.year = '';
									$scope.comment = '';
									$scope.amount = '';
									$scope.paymentType = '';
									$scope.paymentSaved = false;
									$log.info($scope);
								};
								/**
								 * End clear Payment
								 */

								$scope.clearExpense = function() {
									$log.info('Clear Expense Called');
									$scope.paymentSaveSubmitted = false;
									$scope.thePaymentMessage = '';
									$scope.month = '';
									$scope.year = '';
									$scope.comment = '';
									$scope.amount = '';
									$scope.expenseType = '';
									$scope.paymentSaved = false;
									$log.info($scope);
								};
								/**
								 * End clear Payment
								 */
								$scope.drawPayments = function() {

									$http(
											{
												method : 'POST',
												url : 'http://www.arelbou.com/service/user/getYearlySummary',
												data : null
											})
											.success(
													function(data, status,
															headers, config) {
														$log
																.info("Call get getYearlySummary");
														Morris
																.Bar({
																	element : 'bar-example',
																	data : data,
																	xkey : 'year',
																	ykeys : [
																			'in',
																			'out' ],
																	labels : [
																			'Cotisations',
																			'Depenses' ]
																});

													})
											.error(
													function(data, status,
															headers, config) {

														$log
																.info("Call get getYearlySummary Failed");
														$log.info($scope);
													});

								};

							} ])

})();