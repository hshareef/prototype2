/**
 * 
 */

app.controller('ProfileCtrl', function($scope, $http, $routeParams, ClaimService, $location, $window, $timeout, ConfigService, ConstantsService, LookupService) {
	
	var vm = this;
	vm.user = null;
	
	vm.newClaim = null;
	vm.createClaimErrorMessages = [];
	
	//claim error messages - need to eventually move these to the DB ??
	vm.needAtLeastOneCategory = "You must add at least one category to create this claim (it can be changed later).";
	vm.claimStatementEmpty = "Claim statement cannot be empty";
	
	vm.claimStatuses = [
	     {
	    	 id: 1,
	    	 description: 'Preliminary'
	     },
	     {
	    	 id: 2,
	    	 description: 'Published'
	     }
	];
	
	vm.argStatuses = [
	           	     {
	           	    	 id: 1,
	           	    	 description: 'Preliminary'
	           	     },
	           	     {
	           	    	 id: 2,
	           	    	 description: 'Published'
	           	     }
	           	];
	
	vm.argUpdateModes = {
	     create: {
	    	 id: 1,
	    	 description: 'create'
	     },
	     edit: {
	    	 id: 2,
	    	 description: 'edit'
	     }
	                     
	};
	
	vm.userPendingClaims = [];
	vm.userPublishedClaims = [];
	vm.userClaims = [];
	vm.currentClaimStatusId = vm.claimStatuses[0].id;//holds the claim status id that we are currently viewing
	vm.editingClaimDuplicate = null;//the claim you are currently editing
	
	//for Your Arguments tab
	vm.userArgsLoaded = false;
	vm.userPendingArgs = [];
	vm.userPublishedArgs = [];
	vm.userArgs = [];
	vm.currentArgStatusId = vm.argStatuses[0].id;
//	vm.claimsForUserPendingArgs = [];
//	vm.claimsForUserPublishedArgs = [];
	
	vm.editArgDuplicate = null;//when editing an arg, make edits to this duplicate. If save, replace original with this dup and save
	vm.argCreateEditMode = 'edit';//this can either be edit or create. Default to edit for now
	
	vm.activate = function(description, className) {
		var elements = document.getElementsByClassName(className);
		for(var i = 0 ; i < elements.length ; i++){
			if(elements[i].innerHTML == description){
				elements[i].style.backgroundColor = "var(--dark-accent-2)";
			}
			else {
				elements[i].style.backgroundColor = "var(--dark-accent-1-saved)";
			}
		}
	};
	
	vm.publishClaim = function(index) {
		var claim = vm.userPendingClaims[index];
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/updateClaimStatus/" + claim.claimId + "/" + vm.claimStatuses[1].id)
		 .then(function(response){
			if (response.data) {
				//console.log(response.data);
				location.reload();			 
			}
		 });
	};
	
	vm.deleteClaim = function(index) {
		alert("going to try to delete claim of index  " + index);
		var claim = vm.userPendingClaims[index];
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/updateClaimStatus/" + claim.claimId + "/" + 3)//deleted status is 3
		 .then(function(response){
			if (response.data) {
				location.reload();			 
			}
		 });
	};
	
	vm.openArgClaim = function(claimId) {
		$window.location.href = '/Prototype/#/claim/' + claimId; 
	};
	
	 vm.openClaim = function(claimId){
		 if (vm.currentClaimStatusId == vm.claimStatuses[1].id) {
			 //if opening published claim, go to claim page
			$window.location.href = '/Prototype/#/claim/' + claimId; 
		 } else if (vm.currentClaimStatusId == vm.claimStatuses[0].id) {
			 //if opening pending claim, oven claim dialog
			 vm.editingClaimDuplicate = JSON.parse(JSON.stringify(vm.getPendingClaim(claimId)));
			 //vm.editingClaim = vm.getPendingClaim(claimId);
			 vm.openDialog("theEditClaimDialog");
		 }
	 };
	 
    vm.saveClaim = function(){
    	console.log('going to save the claim');
    	console.log(vm.editingClaimDuplicate);
    	$http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/save", vm.editingClaimDuplicate).then(function(response){
    		vm.closeDialog('theEditClaimDialog');
        	var claimId = response.data.claimId;
        	vm.userPendingClaims[vm.getPendingClaimIndex(claimId)] = response.data;//updates claim with edits
    	});
    }; 
    
	vm.removeCategory = function(claim, index) {
		claim.categoryIds.splice(index, 1);
		vm.categorySelected = null;
	};
    
    vm.getPendingClaim = function(claimId) {
    	var claimIndex = vm.getPendingClaimIndex(claimId);
    	if (claimIndex != -1) {
    		return vm.userPendingClaims[claimIndex];
    	} else {
    		return null;
    	}
    };
    
    vm.getPendingClaimIndex = function(claimId) {
    	var claimIndex = -1;
    	vm.userPendingClaims.forEach(function(claim, index){
    		if (claim.claimId == claimId) {
    			claimIndex = index;
    		}
    	});
    	return claimIndex;
    };
	 
	 vm.openDialog = function(dialogId, index){
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "block";
		 if(dialogId == "theCreateEditArgumentDialog"){
			 vm.currentArgIndex = index;
			 vm.editArgDuplicate = JSON.parse(JSON.stringify(vm.userArgs[vm.currentArgIndex]));
		 } else if (dialogId == "theCreateClaimDialog") {
			 vm.newClaim = {};
		 }
	 };
	 
	 vm.closeDialog = function (dialogId) {
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "none";
		 if (dialogId == "theEditClaimDialog") {
			 vm.editingClaimDuplicate = null;
		 } else if (dialogId == "theErrorMessageDialog") {
			 vm.errorMessages = [];//clear the messages so they don't show up anywhere else
		 } else if (dialogId == "theCreateClaimDialog") {
			 vm.createClaimErrorMessages = [];//clear the messages so they don't show up anywhere else
			 vm.categorySelected = null;//reset category options dropdown
		 }
	 };
		 
	vm.changeTab = function(idName, tabIdName){
		var tabPanes = document.getElementsByClassName("tab-pane");
		for(var i = 0 ; i < tabPanes.length ; i++){
			if(tabPanes[i].id !== idName){
				tabPanes[i].style.display = "none";
			}
			else{
				tabPanes[i].style.display = "block";	
			}
		}
		
		if (idName == 'yourClaims') {
			vm.activate('Preliminary', 'status-button');//default to preliminary when loading this tab
		} else if (idName == 'yourArguments'){
			if (!vm.userArgsLoaded) {
				vm.loadUserArguments();
				vm.userArgsLoaded = true;
			}
		}
	};
	
    vm.handleEditArg = function(){
    	vm.validateEditArg(vm.editArgDuplicate);
    	if (vm.errorMessages.length == 0) {
    		vm.userArgs[vm.currentArgIndex] = vm.editArgDuplicate;
    		vm.saveArgument(vm.userArgs[vm.currentArgIndex]);
    		vm.closeDialog('theCreateEditArgumentDialog', false);	
    	} else {
    		alert('we have validation errors');
    	}
    };
    
    vm.saveArgument = function(argument) {
		 $http.post(ConfigService.getSettings().url + "/Prototype/prototype/argument/save", argument).then(function(response){
			 console.log(response);
		 });
    };
    
    vm.addToCurrentArgPremiseArray = function(){
    	var premise = new Object();
    	premise.claimStatement="";
    	if (vm.editArgDuplicate.premises == null) {
    		vm.editArgDuplicate.premises = [];
    	}
    	vm.editArgDuplicate.premises.push(premise);
    };
    
	 vm.searchPremises = function(){
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/searchClaims/" + vm.searchString)
		 .then(function(response){
			 vm.premiseSearchResults = response.data;
		 });
	 };
	 
	 vm.openCreateArgDialog = function() {
		 vm.argCreateEditMode = vm.argUpdateModes.create.id;
		 vm.openDialog('theCreateEditArgumentDialog'); 
	 };
	 
	 vm.addPremiseToArg = function(index){
		 var premise = vm.premiseSearchResults[index];
		 if (vm.argCreateEditMode == 'create') {
			 vm.newArg.premises.push(premise);
		 } else if (vm.argCreateEditMode == 'edit') {
			 vm.editArgDuplicate.premises.push(premise);
		 }
		 vm.closeDialog('theAddPremiseDialog', false);
	 };
	 
	 vm.removePremise = function(argument, premiseIndex){
		 argument.premises.splice(premiseIndex, 1);
	 };
    
	vm.validatePublishArg = function(argument) {
		vm.errorMessages = [];
    	if (argument.argName == null || argument.argName.length == 0) {
    		vm.errorMessages.push(vm.argNeedsName);
    	}
    	
    	if (argument.premises == null || argument.premises.length == 0) {
    		vm.errorMessages.push(vm.argNeedsPremise);
    	}
	};
    
    vm.validateEditArg = function (argument) {
    	vm.errorMessages = [];
    	if (argument.argName == null || argument.argName.length == 0) {
    		vm.errorMessages.push(vm.argNeedsName);
    	}
    };
    
	vm.switchUserClaims = function(statusId) {
		vm.currentClaimStatusId = statusId;
		if (statusId == vm.claimStatuses[0].id) {
			vm.userClaims = vm.userPendingClaims;
		} else if (statusId == vm.claimStatuses[1].id) {
			vm.userClaims = vm.userPublishedClaims;
		}
	};
	
	vm.switchUserArgs = function(statusId) {
		vm.currentArgStatusId = statusId;
		if (statusId == vm.argStatuses[0].id) {
			vm.userArgs = vm.userPendingArgs;
			vm.swapActiveButton("pendingArgsButton", "publishedArgsButton");
		} else if (statusId == vm.argStatuses[1].id) {
			vm.userArgs = vm.userPublishedArgs;
			vm.swapActiveButton("publishedArgsButton", "pendingArgsButton");
		}
	};
	
	//maybe change this later to use css classes
	vm.swapActiveButton = function (activeButtonId, deactiveButtonId) {
		document.getElementById(deactiveButtonId).style.color = "gray";
		document.getElementById(deactiveButtonId).style.background = "#CBBCFA";
		document.getElementById(activeButtonId).style.color = "white";
		document.getElementById(activeButtonId).style.background = "#25274D";
	};
	
	vm.separateClaimsByStatus = function(claims) {
		vm.userPendingClaims = claims.filter(function(claim){
			var pendingFound = false;
			claim.stateHistory.forEach(function(state){
				if (state.currentFlag) {
					pendingFound = state.claimStatusId == vm.claimStatuses[0].id;
				}
			});
			return pendingFound;
		});
		vm.userPublishedClaims = claims.filter(function(claim){
			var publishedFound = false;
			claim.stateHistory.forEach(function(state){
				if (state.currentFlag) {
					publishedFound = state.claimStatusId == vm.claimStatuses[1].id;
				}
			});
			return publishedFound;
		});
	};
	
	vm.separateArgsByStatus = function(args) {
		vm.userPendingArgs = args.filter(function(arg){
			var pendingFound = false;
			arg.stateHistory.forEach(function(state){
				if (state.currentFlag) {
					pendingFound = state.argumentStatusId == vm.argStatuses[0].id;
				}
			});
			return pendingFound;
		});
		vm.userPublishedArgs = args.filter(function(arg){
			var publishedFound = false;
			arg.stateHistory.forEach(function(state){
				if (state.currentFlag) {
					publishedFound = state.argumentStatusId == vm.argStatuses[1].id;
				}
			});
			return publishedFound;
		});
	};
	
	 vm.publishArg = function(index, targetStateId){
		 //can remove targetStateId since this should always be publish
		 //update the state to published
		 //alert("test publish");
		 console.log("trying to publish arg");
		 console.log(vm.userArgs[index]);
		 vm.validatePublishArg(vm.userArgs[index]);
		 if (vm.errorMessages.length == 0) {
			 $http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/updateState/" + targetStateId, vm.userArgs[index]).then(function(response){
				 //after updating arg status, reload page so that it get loaded as a published argument
				 console.log(response);
				 if (response.data) {
					 if (response.data.code == 200) {
						 console.log(response);
						 alert("argument published successfully.")
						 vm.moveArgFromPendingListToPublishedList(index);
					 } else if (response.data.code == 500) {
						 vm.errorMessages = response.data.messages;
						 vm.openDialog('theErrorMessageDialog');
					 } 
				 } else {
					 alert("internal server error occurred, check logs");
				 }
			 });
		 } else {
			 alert("validation errors exist");
		 }
		 

	 };
	
	 vm.moveArgFromPendingListToPublishedList = function(index) {
		 vm.userPublishedArgs.push(vm.userPendingArgs[index]);
		 vm.userPendingArgs.splice(index, 1);
	 };
	 
	 vm.openCreateClaimDialog = function() {
		 vm.openDialog('theCreateClaimDialog');
	 };
	 
	 vm.createNewClaim = function(claim){
		 
		 //clear out error messages
		 vm.createClaimErrorMessages = [];
		//check rules
		 if(claim.categoryIds == null || claim.categoryIds.length == 0){
			 vm.createClaimErrorMessages.push(vm.needAtLeastOneCategory);
		 }
		 if (claim.claimStatement == null || claim.claimStatement == "") {
			 vm.createClaimErrorMessages.push(vm.claimStatementEmpty);
		 }
		 //add more if statements for other rules
		 
		 if (vm.createClaimErrorMessages.length == 0) {
			 vm.createClaimCall(claim);
		 }
	 };
	 
    vm.createClaimCall = function(claim){
    	if(claim.originalOwnerId == null || claim.originalOwnerId == -1){
    		claim.originalOwnerId = vm.user.userId;
    		claim.originalOwnerUsername = vm.user.username;
    	}
    	$http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/create", claim).then(function(response){
        	alert("claim saved!");
        	//vm.claim = response.data;
        	vm.closeDialog('theCreateClaimDialog');
        	vm.userPendingClaims.unshift(claim);
        	vm.userClaims = vm.userPendingClaims;
    	});
    }; 
		   
	 
	 vm.openArg = function(evt, sectionName, index){
	    var i, tabcontent, tablinks;
	    //tabcontent = document.getElementsByClassName("tabcontent");
	    structureSection = document.getElementById('Structure'+index);
	    structureSection.style.display = "none";
	    objectionsSection = document.getElementById('Objections'+index);
	    objectionsSection.style.display = "none";
	    argInfoSection = document.getElementById('ArgInfo'+index);
	    argInfoSection.style.display = "none";
//		    for (i = 0; i < tabcontent.length; i++) {
//		        tabcontent[i].style.display = "none";
//		    }
	    tablinks = document.getElementsByClassName("tablinks");
	    for (i = 0; i < tablinks.length; i++) {
	        tablinks[i].className = tablinks[i].className.replace(" active", "");
	    }
	    document.getElementById(sectionName).style.display = "block";
	    //evt.currentTarget.className += " active";
	 };
		 
	vm.toggleViewIcon = function(index) {
		var ariaExpanded = document.getElementById("arg-header-" + index).getAttribute("aria-expanded");
		if (ariaExpanded == "true") {
			document.getElementById("view-plus-"+index).style.display = "inline";
			document.getElementById("view-minus-"+index).style.display = "none";
		}
		else {
			document.getElementById("view-plus-"+index).style.display = "none";
			document.getElementById("view-minus-"+index).style.display = "inline";
		}
	};
	
	vm.loadUserArguments = function() {
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/argument/userArguments/" + vm.user.userId)
		 .then(function(response){
			 console.log(response.data);
			 vm.separateArgsByStatus(response.data);
			 vm.userArgs = vm.userPendingArgs;//default to pending args
			 vm.swapActiveButton("pendingArgsButton", "publishedArgsButton");//default to pending button
		 });
	};
	
	vm.loadUserClaims = function () {
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/userClaims/" + vm.user.userId)
		 .then(function(response){
			 vm.separateClaimsByStatus(response.data);
			 vm.userClaims = vm.userPendingClaims;//default to pending claims
		 });
	};
	
	vm.addCategory = function(claim, category) {
		console.log("trying to add category");
		console.log(category);
		if(claim.categoryIds == null){
			claim.categoryIds = [];
		}
		if(vm.categorySelectedAlreadyInClaim(claim, category.id)){
			alert("The claim is already part of that category!");
		}
		else{
			claim.categoryIds.push(category.id);
			console.log(claim);
		}
	};
	
	vm.categorySelectedAlreadyInClaim = function(claim, categoryId) {
		for(var i = 0 ; i < claim.categoryIds.length ; i++) {
			if (claim.categoryIds[i] == categoryId) {
				return true;
			}
		}
		return false;
	};
	
//	vm.addCategory = function(category) {
//		console.log("trying to add category");
//		if(vm.editingClaimDuplicate.categoryIds == null){
//			vm.editingClaimDuplicate.categoryIds = [];
//		}
//		if(categorySelectedAlreadyInClaim(category.id)){
//			alert("The claim is already part of that category!");
//		}
//		else{
//			vm.editingClaimDuplicate.categoryIds.push(category.id);
//			console.log(vm.editingClaimDuplicate);
//		}
//	};
//	
//	function categorySelectedAlreadyInClaim (id) {
//		for(var i = 0 ; i < vm.editingClaimDuplicate.categoryIds.length ; i++) {
//			if (vm.editingClaimDuplicate.categoryIds[i] == id) {
//				return true;
//			}
//		}
//		return false;
//	};
	
	vm.init = function() {
		LookupService.getLookup("CLAIM_CATEGORIES_LKUP").then(function(response){
			vm.categoryOptions = response;
			vm.categoryOptionsMap = LookupService.getMappedLookup(vm.categoryOptions);
		});
		vm.user = JSON.parse(localStorage.getItem("user"));
		//vm.activateStatusButton('Your Claims');//default to argument tab, this is bad practice, shouldn't have string hardcoded here
		vm.changeTab('yourClaims', 'dumbvalue');//by default, load claims tab
		vm.activate('Your Claims', 'entity-tab');
		//vm.activateStatusButton(vm.claimStatuses[0].description);//default to pending claims
		vm.loadUserClaims();
	};
	
	vm.init();
	
	 
});