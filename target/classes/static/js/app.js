var app = angular.module('app', []);

app.controller('mainController', function($scope, $http) {
	
	$scope.url = "/";
	$scope.newcompany = {};
	$scope.newbeneficialowners = [];
	$scope.ind = 8;
	$scope.newowner = false;
	$scope.ownerExists = false;
	// if true, add new company(alternative) panel will be shown
	$scope.showAddPanel = false;

	$scope.updateSubmitCompany = function(c) {
		// make http post(update) request here, the company would already be
		// changed, thx to ng-model directive
		$scope.updatecurrentcompany = false;
		var updatedCompany = {
			id : c.id,
			companyname : c.companyname,
			adress : c.adress,
			city : c.city,
			country : c.country,
			phone : c.phone,
			email : c.email

		};
		$http.post("/company", updatedCompany).then(onUpdateCompany, onUpdateCompanyError);
	};

	$scope.updateCompany = function(c) {
		$scope.updatecurrentcompany = !$scope.updatecurrentcompany;

		// c is a companyDto object, so combining it's props we should
		// initialize a new instance of company
		/*
		 * var updatedCompany = { id : c.id, companyname : c.companyname, adress :
		 * c.adress, city : c.city, country : c.country, phone : c.phone, email :
		 * c.email
		 *  };
		 * $http.post("/company",updatedCompany).then(onUpdateCompany,onUpdateCompanyError);
		 * alert("Update success : "+c.companyname);
		 */
	};

	var onUpdateCompany = function(response) {
		alert("updating company is successfull!");
		$scope.companies = response.data;
		$scope.updatecurrentcompany = false;
	};

	var onUpdateCompanyError = function(reason) {
		alert("An error occurred while updating company!");
		$scope.updatecurrentcompany = false;
	};

	// hides the add new beneficial owner panel
	$scope.hideAddPanel = function() {
		$("#addPanel").modal("hide");
	};

	$scope.addOwner = function() {

		$scope.newbeneficialowners.push($scope.currentowner);

		// reset owner textbox and hide newowner div
		$scope.currentowner = "";
		$scope.newowner = false;
		$scope.ownerExists = true;
	};

	$scope.addCurrentOwner = function() {
		var tempOwner = {
			name : $scope.currentowner
		};
		$scope.newbeneficialowners.push();

	};

	$scope.showInnerAddModal = function() {
		$scope.newowner = true;
	}

	$scope.showModal = function() {
		$scope.showAddPanel = !$scope.showAddPanel;
	}

	$scope.addNewCompany = function() {

		$http.post($scope.url + "newcompany", this.newcompany).then(onAddNew,
				onError);
	};

	var onAddNew = function(response) {
		
		$scope.newcompany = {};
		var newbeneficials = [];
		// get the newly added company's id to add the beneficial owners
		var newCompanyId = response.data[response.data.length - 1].id;
		// and time for a new http post request for beneficial owners
		for (var i = 0; i < $scope.newbeneficialowners.length; i++) {
			var tempOwner = {
				name : $scope.newbeneficialowners[i],
				companyid : newCompanyId
			};
			newbeneficials.push(tempOwner);
		}
		$scope.showAddPanel = false;
		
		$http.post($scope.url + "newowner", newbeneficials).then(onNewOwner,
				onNewOwnerError);

	};

	var onNewOwner = function(response) {
		// so that we added beneficial owners, we should clear of the owner list
		$scope.newbeneficialowners = [];
		// this flag is used in a ng-hide directive which is in a div that contains text: 'None' 
		$scope.ownerExists = false;
		$scope.companies = response.data;
		
	};
	var onNewOwnerError = function(reason) {
		alert("An error occurred while adding benefs!");
	}

	var onError = function(reason) {
		alert("An error occurred while adding new company!");
	};

	var onCompleteData = function(response) {
		
		$scope.companies = response.data;
	};

	var onDataError = function(reason) {
		alert("An error occurred while getting companies data!");
	};

	$http.get($scope.url + "companies").then(onCompleteData, onDataError);

});