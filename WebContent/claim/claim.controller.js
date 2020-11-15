/**
 * 
 */

app.controller('ClaimCtrl', function($scope, $http, $routeParams, ClaimService, $location, $window, $timeout, ConfigService, LookupService) {
	
	var vm = this;
	
	vm.testUrl = ConfigService.getSettings().url;
	vm.topClaims = [];
	vm.claim = new Object();
	vm.claim.claimStatement="";
	vm.claim.arguments = [];
	vm.claim.keywords = [];
	vm.claim.originalOwnerId=-1;
	vm.claim.originalOwnerUsername = "";
	vm.editMode = false;
	vm.user = {
			userId: null,
			username: "",
			password: "",
			firstName: "",
			lastName: "",
			emailAddress: "",
			loggedIn: false	
	};
	vm.createNewAcctFlag = false;
	vm.newUser = {
			firstName: "",
			lastName: "",
			username: "",
			password: "",
			emailAddress: "",
			loggedIn: false
		};
	vm.confirmPassword = "";
	
	vm.searchString="";
	
	vm.oppositeClaimSearchResults = [];
	vm.oneUnsavedArgument = false;
	vm.currentArgIndex = null;
	vm.argumentViewingIndex = 0;
	vm.score = 85;
	vm.newArg = null;
	vm.newMpo = null; //MPO stands for Missing Premise Objection
	vm.mpoEditable = false;
	vm.mpoViewingIndex;
	vm.tabs = ['Arguments', 'Opposite Claims', 'Media Resources', 'Claim Info'];
	vm.newMissedPremiseStatement = "";
	vm.openArgSections = {}; //track the open args so that you can toggle css classes
	vm.argCreateEditMode = null;
	vm.editArgDuplicate = null;
	
	//claim error messages - need to eventually move these to the DB
	vm.needAtLeastOneCategory = "You must add at least one category to create this claim (it can be changed later).";
	vm.claimStatementEmpty = "Claim statement cannot be empty";
	vm.mpoNeedsName = "Your missed premise objection must have a name.";
	vm.argNeedsName = "Your argument must have a name";
	vm.argNeedsPremise = "Your argument must have at least one premise.";
	vm.errorMessages = [];

	
//    vm.treedata = [{
//        'id': 1,
//        'title': 'node1',
//        'nodes': [
//          {
//            'id': 11,
//            'title': 'node1.1',
//            'nodes': [
//              {
//                'id': 111,
//                'title': 'node1.1.1',
//                'nodes': []
//              }
//            ]
//          },
//          {
//            'id': 12,
//            'title': 'node1.2',
//            'nodes': []
//          }
//        ]
//      }, {
//        'id': 2,
//        'title': 'node2',
//        'nodrop': true, // An arbitrary property to check in custom template for nodrop-enabled
//        'nodes': [
//          {
//            'id': 21,
//            'title': 'node2.1',
//            'nodes': []
//          },
//          {
//            'id': 22,
//            'title': 'node2.2',
//            'nodes': []
//          }
//        ]
//      }, {
//        'id': 3,
//        'title': 'node3',
//        'nodes': [
//          {
//            'id': 31,
//            'title': 'node3.1',
//            'nodes': []
//          }
//        ]
//      }];
	

    
//    vm.getOptionById = function(lookupType, id){
//    	var options = LookupService.getOptionById(lookupType, id);
//    	return options;
//    };
	
	
	 vm.createNewClaim = function(){
		 
		 //clear out error messages
		 vm.errorMessages = [];
		//check rules
		 if(vm.claim.categoryIds == null || vm.claim.categoryIds.length == 0){
			 vm.errorMessages.push(vm.needAtLeastOneCategory);
		 }
		 if (vm.claim.claimStatement == null || vm.claim.claimStatement == "") {
			 vm.errorMessages.push(vm.claimStatementEmpty);
		 }
		 //add more if statements for other rules
		 
		 if (vm.errorMessages.length == 0) {
			 vm.saveStatement();
		 }
	 };
 
    //for saving a claim - should probably change name to saveClaim or something
    vm.saveStatement = function(){
    	
    	
    	if(vm.claim.originalOwnerId == -1){
    		vm.claim.originalOwnerId = vm.user.userId;
    		vm.claim.originalOwnerUsername = vm.user.username;
    	}
    	var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/create", vm.claim).then(function(response){
        	//alert("message saved!");
        	vm.claim = response.data;
        	vm.editMode=false;
        	vm.oneUnsavedArgument = false;
        	//vm.closeCreateClaimDialog(false);
        	vm.closeDialog('theCreateClaimDialog', false);
        	vm.openClaim(vm.claim.claimId);
    	});

    }; 
    
    vm.deleteClaim = function(){
    	alert("going to delete claim!");
    	$http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/delete/"+vm.claim.claimId).then(function(response){
    		alert("claim deleted!");
    	});
    };
    
    vm.addBlankArg = function(){
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/argTemplate")//think about moving this to front end
		 .then(function(response){
			 vm.newArg = response.data;
			 vm.newArg.ownerId = vm.user.userId;
			 vm.newArg.ownerUsername = vm.user.username;
			 //vm.claim.arguments.push(newArg);
			 //vm.currentArgIndex = vm.claim.arguments.length - 1;
		 });
    };
    
    vm.handleCreateEditArg = function(){
    	vm.validateCreateEditArg();
    	if (vm.errorMessages.length == 0) {
        	if (vm.argCreateEditMode == 'create') {
        		vm.addNewArg();
        	} else if (vm.argCreateEditMode == 'edit') {
            	//vm.saveStatement();
        	}
    		vm.saveStatement();
    		vm.closeDialog('theCreateEditArgumentDialog', false);	
    	} else {
    		alert('we have calidation errors');
    	}
    };
    
    vm.validateCreateEditArg = function () {
    	vm.errorMessages = [];
    	if (vm.argCreateEditMode == 'create') {
        	if (vm.newArg.argName == null || vm.newArg.argName.length == 0) {
        		vm.errorMessages.push(vm.argNeedsName);
        	}
        	
        	if (vm.newArg.premises == null || vm.newArg.premises.length == 0) {
        		vm.errorMessages.push(vm.argNeedsPremise);
        	}
    	} else if (vm.argCreateEditMode == 'edit') {
        	if (vm.claim.arguments[vm.currentArgIndex].argName == null || vm.claim.arguments[vm.currentArgIndex].argName.length == 0) {
        		vm.errorMessages.push(vm.argNeedsName);
        	}
        	
        	if (vm.claim.arguments[vm.currentArgIndex].premises == null || vm.claim.arguments[vm.currentArgIndex].premises.length == 0) {
        		vm.errorMessages.push(vm.argNeedsPremise);
        	}
    	}
    };
    
    vm.addNewArg = function(){
		if (vm.claim.arguments == null) {
			vm.claim.arguments = [];
		}
		vm.claim.arguments.push(vm.newArg);
		vm.currentArgIndex = vm.claim.arguments.length;//look into if vm.currentArgIndex is still being used
    };
    
//    vm.addNewArgAndSaveClaim = function(){
//    	
//    	//clear out error messages
//    	vm.errorMessages = [];
//    	
//    	if (vm.newArg.argName == null || vm.newArg.argName.length == 0) {
//    		vm.errorMessages.push(vm.argNeedsName);
//    	}
//    	
//    	if (vm.newArg.premises == null || vm.newArg.premises.length == 0) {
//    		vm.errorMessages.push(vm.argNeedsPremise);
//    	}
//    	
//    	if (vm.errorMessages.length == 0) {
//        	if (vm.claim.arguments == null) {
//        		vm.claim.arguments = [];
//        	}
//        	vm.claim.arguments.push(vm.newArg);
//        	vm.currentArgIndex = vm.claim.arguments.length;//look into if vm.currentArgIndex is still being used
//        	vm.saveStatement();
//        	vm.closeDialog('theCreateEditArgumentDialog', false);
//    	}
//    	
//    };
    
    vm.addAndSaveNewMpo = function(){
    	//clear error messages
    	vm.errorMessages = [];
    	//check rules first
    	if(vm.newMpo.name == null || vm.newMpo.name.length == 0){
    		vm.errorMessages.push(vm.mpoNeedsName);
    	}
    	if(vm.errorMessages.length == 0){
        	vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections.push(vm.newMpo);
        	vm.saveStatement();
        	vm.closeDialog('theAddMpoDialog', false)
    	}

    };
    
    vm.addToArgumentArray = function(){
    	var argument = new Object();
    	argument.argName="default name";
    	argument.premises = [];
    	argument.stateHistory = [
    	                           {
    	                        	   argumentStateId: null,
    	                        	   createdTs: null,
    	                        	   updatedTs: null,
    	                        	   argumentStatusId: 1,
    	                        	   currentFlag: true
    	                           }
    	                           ];
    	argument.ownerId = vm.user.userId;
    	argument.ownerUsername = vm.user.username;
    	argument.editable = true;
    	argument.valid = false;
    	argument.sound = false;
    	argument.validCount = 0;
    	argument.invalidCount = 0;
    	argument.fallacyDetails = {
    			adHomniemUpvotes: 0,
    			adHomniemDownvotes: 0,
    			ignorantumUpvotes: 0,
    			ignorantumDownvotes: 0,
    			adpopulumUpvotes: 0,
    			adpopulumDownvotes: 0,
    			argFromAuthorityUpvotes: 0,
    			argFromAuthorityDownvotes: 0,
    			generalizationUpvotes: 0,
    			generalizationDownvotes: 0,
    			slipperySlopeUpvotes: 0,
    			slipperSlopeDownvotes: 0,
    			strawmanUpvotes: 0,
    			strawmanDownvotes: 0,
    			redHerringUpvotes: 0,
    			redHerringDownvotes: 0,
    			falseDichotomyUpvotes: 0,
    			falseDichotomyDownvotes: 0,
    			circularReasoningUpvotes: 0,
    			circularReasoningDownvotes: 0
    	};
    	vm.claim.arguments.push(argument);
    	vm.oneUnsavedArgument = true;
    };
    
    vm.deleteArgument = function(index){
    	vm.claim.arguments.splice(index, 1);
    	vm.saveStatement();
    };
    
    vm.setArgumentEditMode = function(index, editMode){//editMode is bool
    	if(vm.claim.arguments[index].ownerId === vm.user.userId){
    		vm.claim.arguments[index].editable = editMode;
    	}
    };
    
    vm.changeArgViewing = function(index){
    	//this argument viewing index way doesn;t make sense anymore cause we can now view multiple arguments at once
    	//need to rework this
    	vm.argumentViewingIndex = index;
    };
    
    vm.addKeyword = function(){
    	var keyword = "keyword";
    	vm.claim.keywords.push(keyword);
    };
    
    vm.deleteKeyword = function(index){
    	vm.claim.keywords.splice(index, 1);
    	vm.saveStatement();
    };
    
    
    vm.addToPremiseArray = function(argIndex){
    	var premise = new Object();
    	premise.claimStatement="";
    	//premise.originalOwnerId = vm.user.userId;
    	//premise.originalOwnerUsername = vm.user.username;
    	vm.claim.arguments[argIndex].premises.push(premise);
    };
    
    vm.addToCurrentArgPremiseArray = function(){
    	var premise = new Object();
    	premise.claimStatement="";
    	//premise.originalOwnerId = vm.user.userId;
    	//premise.originalOwnerUsername = vm.user.username;
    	vm.claim.arguments[vm.currentArgIndex].premises.push(premise);
    };
    
    vm.addToNewArgPremiseArray = function(){
    	var premise = new Object();
    	premise.claimStatement = "";
    	vm.newArg.premises.push(premise);
    };
    
    vm.addToArgMissedPremiseArray = function(){
    	var missedPremise = new Object();
    	missedPremise.claimStatement = "";
    	vm.newMpo.missedPremises.push(missedPremise);
    	//vm.claim.arguments[vm.currentArgIndex].missedPremiseObjections.push(missedPremise);
    };
    
    vm.pushNewMissedPremiseToMPO = function(){
    	var missedPremise = new Object();
    	missedPremise.claimStatement = vm.newMissedPremiseStatement;
    	missedPremise.canDelete = true;
    	missedPremise.deleteTracker = vm.newMpo.allMpoPremises.length; //need this since ui-tree cannot use track by index (baloney!)
    	vm.newMpo.missedPremises.push(missedPremise);//is missedPremises needed?
    	vm.newMpo.allMpoPremises.push(missedPremise);
    	vm.closeDialog('theAddNewMissedPremiseDialog', false);
    };
    
    vm.removeMissedPremise = function(deleteTracker){
    	for(var i = 0 ; i < vm.newMpo.allMpoPremises.length ; i++){
    		if(vm.newMpo.allMpoPremises[i].deleteTracker && vm.newMpo.allMpoPremises[i].deleteTracker === deleteTracker){
    			vm.newMpo.allMpoPremises.splice(i, 1);
    			break;
    		}
    	}
    };
    
    vm.getCurrentStatusId = function(argument){
    	for(var i=0; i < argument.stateHistory.length; i++){
    		if(argument.stateHistory[i].currentFlag){
    			return argument.stateHistory[i].argumentStatusId;
    		}
    	}
    };
    
    vm.getFirstPublishedArg = function(){
    	for(var i = 0 ; i < vm.claim.arguments.length ; i++){
    		for(var j = 0; j < vm.claim.arguments[i].stateHistory.length ; j++){
    			if(vm.claim.arguments[i].stateHistory[j].currentFlag && vm.claim.arguments[i].stateHistory[j].argumentStatusId === 2){
    				return i;
    			}
    		}
    	}
    	return null;
    };
    
    vm.setEditMode = function(){
    	vm.editMode = true;
    };
    
	
	
//	 vm.loadTopClaims = function(){
//		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/topClaims")
//		 .then(function(response){
//			 vm.topClaims = response.data;
//		 });
//	    };
	    
	    
		
		 vm.getClaim = function(claimId){
			 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/" + claimId)
			 .then(function(response){
				 vm.claim = response.data;
				 console.log(vm.claim);
			 });
		    };
	 
	 vm.callClaimServiceFunction = function(){
		 alert("Going to call the claim service function, or not...maybe");
	 };
	 
	 vm.openClaim = function(claimId){
		$window.location.href = '/Prototype/#/claim/' + claimId;
		
	 };
	 
	 vm.searchClaims = function(){
		 //alert(vm.searchString);
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/searchClaims/" + vm.searchString)
		 .then(function(response){
			 vm.topClaims = response.data;
		 });
	 };
	 
//	 vm.voteValidity = function(index, valid){
//		 alert("trying to vote as valid");
//		 if(valid){
//			 if(vm.claim.arguments[index].validCount === undefined || vm.claim.arguments[index].validCount === null){
//				 vm.claim.arguments[index].validCount = 1;
//			 }
//			 else {
//				 vm.claim.arguments[index].validCount += 1;
//			 }
//		 }
//		 else{
//			 if(vm.claim.arguments[index].invalidCount === undefined || vm.claim.arguments[index].invalidCount === null){
//				 vm.claim.arguments[index].invalidCount = 1;
//			 }
//			 else {
//				 vm.claim.arguments[index].invalidCount += 1;
//			 }
//		 }
//		 vm.saveStatement();
//	 };
	 
	 //need to rename this to like openTab or something
	 vm.openCity = function(evt, sectionName, index){
	    var i, tabcontent, tablinks;
	    //tabcontent = document.getElementsByClassName("tabcontent");
	    structureSection = document.getElementById('Structure'+index);
	    structureSection.style.display = "none";
	    objectionsSection = document.getElementById('Objections'+index);
	    objectionsSection.style.display = "none";
	    argInfoSection = document.getElementById('ArgInfo'+index);
	    argInfoSection.style.display = "none";
//	    for (i = 0; i < tabcontent.length; i++) {
//	        tabcontent[i].style.display = "none";
//	    }
	    tablinks = document.getElementsByClassName("tablinks");
	    for (i = 0; i < tablinks.length; i++) {
	        tablinks[i].className = tablinks[i].className.replace(" active", "");
	    }
	    document.getElementById(sectionName).style.display = "block";
	    //evt.currentTarget.className += " active";
	 };
	 setTimeout(function(){
		 var defaultSections = document.getElementsByClassName("defaultOpen");
		 for(var j = 0 ; j < defaultSections.length ; j++){
			 defaultSections[j].click();
		 }
	}, 2000);
	 
	 
//	 vm.voteFallacy = function(type, index){
//		 alert("inside the vote");
//		 vm.claim;
//		 if(type === 'adHomniem'){
//			 vm.claim.arguments[index].fallacyDetails.adHomniemUpvotes =
//				 vm.claim.arguments[index].fallacyDetails.adHomniemUpvotes+1;
//			 var t=9;
//			 vm.claim;
//		 }
//		 else if(type === 'ignorantum'){
//			 vm.claim.arguments[index].fallacyDetails.ignorantumUpvotes =
//				 vm.claim.arguments[index].fallacyDetails.ignorantumUpvotes+1;
//		 }
//		 else if(type === 'adpopulum'){
//			 vm.claim.arguments[index].fallacyDetails.adpopulumUpvotes +=1;	 
//		 }
//		 else if(type === 'argFromAuthority'){
//			 vm.claim.arguments[index].fallacyDetails.argFromAuthorityUpvotes +=1;
//		 }
//		 else if(type === 'generalization'){
//			 vm.claim.arguments[index].fallacyDetails.generalizationUpvotes +=1;
//		 }
//		 else if(type === 'slipperySlope'){
//			 vm.claim.arguments[index].fallacyDetails.slipperySlopeUpvotes +=1;
//		 }
//		 else if(type === 'strawman'){
//			 vm.claim.arguments[index].fallacyDetails.strawmanUpvotes +=1;
//		 }
//		 else if(type === 'redHerring'){
//			 vm.claim.arguments[index].fallacyDetails.redHerringUpvotes +=1;
//		 }
//		 else if(type === 'falseDichotomy'){
//			 vm.claim.arguments[index].fallacyDetails.falseDichotomyUpvotes +=1;
//		 }
//		 else if(type === 'circularReasoning'){
//			 vm.claim.arguments[index].fallacyDetails.circularReasoningUpvotes +=1;
//		 }
//		 vm.saveStatement();
//		 
//	 };

//	 vm.openOppositeClaimDialog = function(){
//		 var theOppoClaimDialog = document.getElementById('theOppoClaimDialog');
//		 theOppoClaimDialog.style.display = "block";
//	 };
//	 
//	 vm.closeOppositeClaimDialog = function(){
//		 var theOppoClaimDialog = document.getElementById('theOppoClaimDialog');
//		 theOppoClaimDialog.style.display = "none";
//	 };
//	 
//	 vm.openCreateClaimDialog = function(){
//		 var theCreateClaimDialog = document.getElementById('theCreateClaimDialog');
//		 theCreateClaimDialog.style.display = "block";
//	 };
//	 
//	 vm.closeCreateClaimDialog = function(redirect){
//		 var theCreateClaimDialog = document.getElementById('theCreateClaimDialog');
//		 theCreateClaimDialog.style.display = "none";
//		 if(redirect){
//			 window.location.href = window.history.back(1);
//		 }
//	 };
	 
	 vm.openDialog = function(dialogId, argIndex, dialogMode, mpoIndex){
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "block";
		 
		 if(dialogId == "theCreateEditArgumentDialog"){
			 vm.argCreateEditMode = dialogMode;
			 if (vm.argCreateEditMode == "create") {
				 vm.addBlankArg();
			 } else if (vm.argCreateEditMode == "edit") {
				 vm.currentArgIndex = argIndex;
				 //vm.editArgDuplicate = vm.claim.arguments[vm.currentArgIndex];
			 }
		 }
		 else if(dialogId == "theCreateNewMediaResourceDialog"){
			 vm.newMediaResource = 
				 {
					resourceTypeId : 1,
					url : "",
					publishDate : "",
					author : null,
					institution : null
				 };
		 }
		 else if(dialogId == "theAddMpoDialog"){
			 vm.argumentViewingIndex = argIndex;
			 vm.mpoViewingIndex = mpoIndex;
			 if(vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections != null &&
					 vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections.length > 0){
				 vm.orderMpoPremises();
			 }
			 if(dialogMode == null || dialogMode == "addNewMpo"){
				 vm.mpoEditable = true;
			 	 vm.initializeBlankMpo();
		 	 }
			 else if (dialogMode == "viewMpo") {
				 vm.mpoEditable = false;
			 }
		 }
		 

		 
	 };
	 
	 vm.closeDialog = function(dialogId, redirect, destination){
		 vm.errorMessages = [];//maybe rename to dialogErrorMessages or something
		 console.log(vm.claim);
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "none";
		 if(dialogId == "theCreateNewMediaResourceDialog"){
			 vm.newMediaResource = null;
		 }
		 if(destination !== undefined && destination !== null){
			 window.location.href = ConfigService.getSettings().url + "/Prototype/#/" + destination;
		 }
		 else if(redirect){
			 window.location.href = window.history.back(1);
		 }
	 };
	 
	 //change this to a get template backend rest call?
	 vm.initializeBlankMpo = function(){
		 vm.newMpo = {
				 name: "",
				 missedPremises: [],
				 allMpoPremises: [],
				 ownerId: vm.user.userId
		 };
		 vm.newMpo.allMpoPremises = angular.copy(vm.claim.arguments[vm.argumentViewingIndex].premises);
		 //vm.claim.missedPremiseObjections.push(newMpo);
	 };
	 
	 vm.orderMpoPremises = function(){
		 //purpose of this function: integrate the original argument premises with the missed premises in the order that the user
		 //has specified. If no order specified, then just display the missed premises after the original argument premises
		 var orderedPremises = [];
		 var order = vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections[vm.mpoViewingIndex].premiseOrder;
		 var argPremises = vm.claim.arguments[vm.argumentViewingIndex].premises;
		 var mpoPremises = vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections[vm.mpoViewingIndex].missedPremises;
		 var allPremises = argPremises.concat(mpoPremises);
		 if(order != null && order.length > 0){
			 for(var i = 0 ; i < order.length ; i++){
				 for(var j = 0 ; j < allPremises.length ; j++){
					 if(order[i].claimId === allPremises[j].claimId){
						 orderedPremises.push(allPremises[j]);
						 break;
					 }
				 }
			 }
			 vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections[vm.mpoViewingIndex].allMpoPremises = orderedPremises;
		 }
		 else{
			 vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections[vm.mpoViewingIndex].allMpoPremises = allPremises;
		 }
	 };
	 
	 
	 vm.searchOppositeClaims = function(){
		 alert(vm.searchString);
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/searchClaims/" + vm.searchString)
		 .then(function(response){
			 vm.oppositeClaimSearchResults = response.data;
		 });
	 };
	 
	 vm.searchPremises = function(){
		 alert(vm.searchString);
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/searchClaims/" + vm.searchString)
		 .then(function(response){
			 vm.premiseSearchResults = response.data;
		 });
	 };
	 
	 vm.setOppositeClaim = function(claimId, oppositeClaimId){
		alert("this claim id: " + claimId + "\nOpposite claim Id: " + oppositeClaimId); 
		$http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/opposites/" + oppositeClaimId, vm.claim).then(function(response){
			vm.claim = response.data;
		});
		vm.closeDialog('theOppoClaimDialog', false);
		//location.reload();
		
	 };
	 
//	 vm.addPremiseToEditableClaim = function(index){
////		alert("this claim id: " + claimId); 
////		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/" + claimId)
////		 .then(function(response){
////			 var premise = response.data;
////			 premise.usedAsPremise = true;
////			 vm.claim.arguments[vm.currentArgIndex].premises.push(premise);
////			 alert("searched premise added successfully!");
////			 vm.closeDialog('theAddPremiseDialog', false);
////		 });
//		 var premise = vm.premiseSearchResults[index];
//		 vm.claim.arguments[vm.currentArgIndex].premises.push(premise);
//		 vm.closeDialog('theAddPremiseDialog', false);
//	 };
	 
	 vm.removeUnsavedPremise = function(index){
		 vm.newArg.premises.splice(index, 1);
		 alert(index);
	 };
	 
	 vm.addPremiseToArg = function(index){
		 var premise = vm.premiseSearchResults[index];
		 if (vm.argCreateEditMode == 'create') {
			 vm.newArg.premises.push(premise);
		 } else if (vm.argCreateEditMode == 'edit') {
			 vm.claim.arguments[vm.currentArgIndex].premises.push(premise);
		 }
		 vm.closeDialog('theAddPremiseDialog', false);
	 };
		 
	 vm.addMissedPremiseToMpo = function(index){
		 var premise = vm.premiseSearchResults[index];
		 premise.canDelete = true;
		 premise.deleteTracker = vm.newMpo.allMpoPremises.length; //need this since ui-tree cannot use track by index (baloney!)
		 vm.newMpo.missedPremises.push(premise);
		 vm.newMpo.allMpoPremises.push(premise);
		 vm.closeDialog('theAddMissedPremiseDialog', false);
	 };
	 
	 vm.publishArg = function(index, targetStateId){
		 //update the state to published
		 //alert("test publish");
		 $http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/updateState/" + targetStateId, vm.claim.arguments[index]).then(function(response){
			 vm.claim.arguments[index] = response.data;
		 });
	 };
	 
	 function getUrlVariable(name){
		   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
		      return decodeURIComponent(name[1]);
	};
	
	function getPageName(longString){
		if(longString.indexOf("?") > 0){
			return longString.substring(longString.indexOf("Prototype"), longString.indexOf("?")); 
		}
		else{
			return longString.substring(longString.indexOf("Prototype"));
		}
	};
	
	vm.login = function () {
		alert("trying to log in..." + vm.user.username + " : " +  vm.user.password);
		var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/login", vm.user)
		.then(function(response){
			vm.user = response.data;
			if(vm.user === undefined || vm.user === null){
				alert("login not successful");
			}
			else{
				alert("login successful.");
				localStorage.setItem("userId", vm.user.userId)
				$window.location.href = "/Prototype/index.html";
			}
		});
		
	};
	
	vm.logout = function(){
		alert("trying to log out!");
		localStorage.clear();
		location.reload();
	};
	
	vm.getUser = function(userId){
		$http.get(ConfigService.getSettings().url + "/Prototype/prototype/login/"+userId)
		.then(function(response){
			vm.user = response.data;
		});
	};
	
	vm.setNewAcctFlag = function(flag){
		vm.createNewAcctFlag = flag;
		if(flag){

		}
		else{
			vm.newUser = null;
			vm.confirmPassword = null;
		}
			
	};
	
	vm.createNewUserAcct = function(){
		alert("trying to create new user account...");
		if(vm.newUser.password !== vm.confirmPassword){
			alert("passwords don't match");
		}else{
			var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/login/createUser", vm.newUser);
		}
	};
	
	vm.changeTab = function(idName, tabIdName){
		
		//var activeTab = document.getElementById(idName);
//		var tabHeaders = document.getElementsByClassName("tab-header");
//		for(var j = 0 ; j < tabHeaders.length ; j++){
//			if(tabHeaders[j].id !== tabIdName){
//				//tabHeaders[j].classList.remove("tab-active");
//				tabHeaders[j].style.backgroundColor = "var(--inactive-tab-header)";
//			}
//			else{
//				//tabHeaders[j].className += "tab-active";
//				tabHeaders[j].style.backgroundColor = "var(--tab-color)";
//			}
//		}
		
		var tabPanes = document.getElementsByClassName("tab-pane");
		for(var i = 0 ; i < tabPanes.length ; i++){
			if(tabPanes[i].id !== idName){
				tabPanes[i].style.display = "none";
			}
			else{
				tabPanes[i].style.display = "block";	
			}
		}
		
		if(idName == "claimInfo"){
			//stuff to do when opening claim info tab
			//keyword stuff
		    var keywordInput = document.getElementById("keyword-input");
		    
		    keywordInput.addEventListener("keyup", function(event) {
	    	  // Number 13 is the "Enter" key on the keyboard
	    	  if (event.keyCode === 13) {
	    	      // Cancel the default action, if needed
	    	      //event.preventDefault();
	    	      // Trigger the button element with a click
	    	      //document.getElementById("myBtn").click();
	    		  // var keyword = keywordInput.value;
	    		  // alert(keyword);
	    		  vm.addKeyword(keywordInput.value);
	    		  document.getElementById("keyword-input").value = "";
	    	  }
	    	});
		}
	};
	
	vm.addKeyword = function(keyword){
		//need to also check if keyword already exists
		if(keyword != null && keyword != ""){
			vm.claim.keywords.push(keyword);
			vm.saveStatement();
		}
	};
	
	vm.removeKeyword = function(index){
		vm.claim.keywords.splice(index, 1);
		vm.saveStatement();
	};
	
	vm.addCategory = function(category, save) {
		if(vm.claim.categoryIds == null){
			vm.claim.categoryIds = [];
		}
		if(categorySelected(category.id)){
			alert("The claim is already part of that category!");
		}
		else{
			vm.claim.categoryIds.push(category.id);
			if(save){
				vm.saveStatement();
			}
		}
	};
	
	vm.removeCategory = function(index, save) {
		vm.claim.categoryIds.splice(index, 1);
		if (save) {
			vm.saveStatement();
		}
	};
	
	function categorySelected (id) {
		for(var i = 0 ; i < vm.claim.categoryIds.length ; i++) {
			if (vm.claim.categoryIds[i] == id) {
				return true;
			}
		}
		return false;
	};
	
	//probably want to just set class of the button insteead of doing this
	vm.activateCategoryButton = function(description) {
		var categoryButtons = document.getElementsByClassName("category-button");
		for(var i = 0 ; i < categoryButtons.length ; i++){
			if(categoryButtons[i].innerHTML == description){
				categoryButtons[i].style.backgroundColor = "var(--dark-accent-2)";
			}
			else {
				categoryButtons[i].style.backgroundColor = "var(--dark-accent-1-saved)";
			}
		}
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
	
//	vm.showMinusIcon = function(index) {
//		var element = document.getElementById("arg-header-" + index);
//		var expanded = element.getAttribute("aria-expanded");
//		return expanded == "true";
//		//return document.getElementById("arg-header-" + index).getAttribute("aria-expanded");
//	};
	
//	vm.forceDigest = function() {
//		//$scope.$apply();
//		//vm.forcer = 1;
//	};
	 
	 vm.init = function(){
		LookupService.getLookup("CLAIM_CATEGORIES_LKUP").then(function(response){
			vm.categoryOptions = response;
			vm.categoryOptionsMap = LookupService.getMappedLookup(vm.categoryOptions);
			vm.getUser(localStorage.getItem("userId"));
			var claimId = $routeParams.claimId;//getUrlVariable("claimId");
			if(claimId === undefined || claimId === null){
				$timeout(function(){
					vm.openDialog('theCreateClaimDialog');
				});
				 
			}
			else {
				vm.getClaim(claimId);
				vm.activateCategoryButton('Arguments');//default to argument tab, this is bad practice, shouldn't have string hardcoded here
				vm.changeTab('arguments', 'argumentsTab');//by default, load arguments tab
			}
		});
	 };
	 
	 vm.init();
	 
});