
claimApp.controller('ClaimCtrl', function($scope, $http, ClaimService, $location, $window, $timeout, ConfigService) {
	
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
	
    vm.treedata = [{
        'id': 1,
        'title': 'node1',
        'nodes': [
          {
            'id': 11,
            'title': 'node1.1',
            'nodes': [
              {
                'id': 111,
                'title': 'node1.1.1',
                'nodes': []
              }
            ]
          },
          {
            'id': 12,
            'title': 'node1.2',
            'nodes': []
          }
        ]
      }, {
        'id': 2,
        'title': 'node2',
        'nodrop': true, // An arbitrary property to check in custom template for nodrop-enabled
        'nodes': [
          {
            'id': 21,
            'title': 'node2.1',
            'nodes': []
          },
          {
            'id': 22,
            'title': 'node2.2',
            'nodes': []
          }
        ]
      }, {
        'id': 3,
        'title': 'node3',
        'nodes': [
          {
            'id': 31,
            'title': 'node3.1',
            'nodes': []
          }
        ]
      }];
    
    
    
	
 
    //for saving a claim
    vm.saveStatement = function(){
    	
    	
    	//if(vm.claim.originalOwnerId=-1){
    		//vm.claim.originalOwnerId = vm.user.userId;
    		//vm.claim.originalOwnerUsername = vm.user.username;
    	//}
    	var test = $http.post(ConfigService.getSettings().url + "/Prototype/prototype/claim/create", vm.claim).then(function(response){
        	alert("message saved!");
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
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/argTemplate")
		 .then(function(response){
			 vm.newArg = response.data;
			 vm.newArg.ownerId = vm.user.userId;
			 vm.newArg.ownerUsername = vm.user.username;
			 //vm.claim.arguments.push(newArg);
			 //vm.currentArgIndex = vm.claim.arguments.length - 1;
		 });
    };
    
    vm.addAndSaveNewArgument = function(){
    	vm.claim.arguments.push(vm.newArg);
    	vm.currentArgIndex = vm.claim.arguments.length;
    	vm.saveStatement();
    };
    
    vm.addAndSaveNewMpo = function(){
    	vm.claim.arguments[vm.argumentViewingIndex].missedPremiseObjections.push(vm.newMpo);
    	vm.saveStatement();
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
    
	
	
	 vm.loadTopClaims = function(){
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/topClaims")
		 .then(function(response){
			 vm.topClaims = response.data;
		 });
	    };
	    
	    
		
		 vm.getClaim = function(claimId){
			 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/" + claimId)
			 .then(function(response){
				 vm.claim = response.data;
				 vm.argumentViewingIndex = vm.getFirstPublishedArg();
			 });
		    };
	    
	 vm.createNewClaim = function(){
		 
	 };
	 
	 vm.callClaimServiceFunction = function(){
		 alert("Going to call the claim service function, or not...maybe");
	 };
	 
	 vm.openClaim = function(claimId){
		$window.location.href = '/Prototype/createForm.html?claimId=' + claimId;
		
	 };
	 
	 vm.searchClaims = function(){
		 alert(vm.searchString);
		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/searchClaims/" + vm.searchString)
		 .then(function(response){
			 vm.topClaims = response.data;
		 });
	 };
	 
	 vm.voteValidity = function(index, valid){
		 alert("trying to vote as valid");
		 if(valid){
			 if(vm.claim.arguments[index].validCount === undefined || vm.claim.arguments[index].validCount === null){
				 vm.claim.arguments[index].validCount = 1;
			 }
			 else {
				 vm.claim.arguments[index].validCount += 1;
			 }
		 }
		 else{
			 if(vm.claim.arguments[index].invalidCount === undefined || vm.claim.arguments[index].invalidCount === null){
				 vm.claim.arguments[index].invalidCount = 1;
			 }
			 else {
				 vm.claim.arguments[index].invalidCount += 1;
			 }
		 }
		 vm.saveStatement();
	 };
	 
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
	 
	 
	 vm.voteFallacy = function(type, index){
		 alert("inside the vote");
		 vm.claim;
		 if(type === 'adHomniem'){
			 vm.claim.arguments[index].fallacyDetails.adHomniemUpvotes =
				 vm.claim.arguments[index].fallacyDetails.adHomniemUpvotes+1;
			 var t=9;
			 vm.claim;
		 }
		 else if(type === 'ignorantum'){
			 vm.claim.arguments[index].fallacyDetails.ignorantumUpvotes =
				 vm.claim.arguments[index].fallacyDetails.ignorantumUpvotes+1;
		 }
		 else if(type === 'adpopulum'){
			 vm.claim.arguments[index].fallacyDetails.adpopulumUpvotes +=1;	 
		 }
		 else if(type === 'argFromAuthority'){
			 vm.claim.arguments[index].fallacyDetails.argFromAuthorityUpvotes +=1;
		 }
		 else if(type === 'generalization'){
			 vm.claim.arguments[index].fallacyDetails.generalizationUpvotes +=1;
		 }
		 else if(type === 'slipperySlope'){
			 vm.claim.arguments[index].fallacyDetails.slipperySlopeUpvotes +=1;
		 }
		 else if(type === 'strawman'){
			 vm.claim.arguments[index].fallacyDetails.strawmanUpvotes +=1;
		 }
		 else if(type === 'redHerring'){
			 vm.claim.arguments[index].fallacyDetails.redHerringUpvotes +=1;
		 }
		 else if(type === 'falseDichotomy'){
			 vm.claim.arguments[index].fallacyDetails.falseDichotomyUpvotes +=1;
		 }
		 else if(type === 'circularReasoning'){
			 vm.claim.arguments[index].fallacyDetails.circularReasoningUpvotes +=1;
		 }
		 vm.saveStatement();
		 
	 };

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
		 
		 //if its the add argument dialog box
		 if(dialogId == "theCreateNewArgumentDialog"){
			 vm.addBlankArg();
		 }
		 else if(dialogId == "theEditArgumentDialog"){
			 vm.currentArgIndex = argIndex;
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
			 vm.orderMpoPremises();
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
		 var theDialogBox = document.getElementById(dialogId);
		 theDialogBox.style.display = "none";
		 if(dialogId == "theCreateNewMediaResourceDialog"){
			 vm.newMediaResource = null;
		 }
		 if(destination !== undefined && destination !== null){
			 window.location.href = ConfigService.getSettings().url + "/Prototype/" + destination;
		 }
		 else if(redirect){
			 window.location.href = window.history.back(1);
		 }
	 };
	 
	 vm.initializeBlankMpo = function(){
		 vm.newMpo = {
				 name: "",
				 missedPremises: [],
				 allMpoPremises: []
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
	 
	 vm.addPremiseToEditableClaim = function(index){
//		alert("this claim id: " + claimId); 
//		 $http.get(ConfigService.getSettings().url + "/Prototype/prototype/claim/" + claimId)
//		 .then(function(response){
//			 var premise = response.data;
//			 premise.usedAsPremise = true;
//			 vm.claim.arguments[vm.currentArgIndex].premises.push(premise);
//			 alert("searched premise added successfully!");
//			 vm.closeDialog('theAddPremiseDialog', false);
//		 });
		 var premise = vm.premiseSearchResults[index];
		 vm.claim.arguments[vm.currentArgIndex].premises.push(premise);
		 alert("searched premise added successfully!");
		 vm.closeDialog('theAddPremiseDialog', false);
	 };
	 
	 vm.addPremiseToClaim = function(index){
			 var premise = vm.premiseSearchResults[index];
			 vm.newArg.premises.push(premise);
			 alert("searched premise added successfully to new argument!");
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
		 alert("test publish");
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
		var test = $http.get(ConfigService.getSettings().url + "/Prototype/prototype/login/"+userId)
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
	 
	 vm.init = function(){
		 if(getPageName(window.location.toString()) == "Prototype/index.html"){
			 if(localStorage.getItem("userId") !== undefined && localStorage.getItem("userId") !== null){
				 vm.getUser(localStorage.getItem("userId"));
			 }
			 vm.loadTopClaims();
		 }
		 if(getPageName(window.location.toString()) == "Prototype/createForm.html"){
			 //vm.user.userId = localStorage.getItem("userId");
			 vm.getUser(localStorage.getItem("userId"));
			 var claimId = getUrlVariable("claimId");
			 if(claimId === undefined || claimId === null){
//				 setTimeout(function(){
//					 vm.openDialog('theCreateClaimDialog');
//					}, 2000);
//				 document.onLoad = function(){
//					 vm.openDialog('theCreateClaimDialog');
//				 };
				 $timeout(function(){
					 vm.openDialog('theCreateClaimDialog');
				 });
				 
			 }
			 else {
				 vm.getClaim(claimId);
			 }
		 }
	 };
	 
	 vm.init();
    
    
});