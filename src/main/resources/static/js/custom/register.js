function register() {
	console.log("register");
	var body = {};
	body.login = 		document.getElementById("registerLogin").value;
	body.password =		document.getElementById("registerPassword").value;
	body.email = 		document.getElementById("registerEmail").value;
	body.firstName = 	document.getElementById("registerFirstName").value;
	body.middleName = 	document.getElementById("registerMiddleName").value;
	body.lastName = 	document.getElementById("registerFamilyName").value;
	var request = apiAccount;
	if (body.login != "" && body.pass != "" && body.email != "") {
		processAjax("registration", 'POST', request, body, callbackTypeRegisterSuccess);
	}	
}

/*
Redirects on success page after registration, else throws an error
*/
function callbackTypeRegisterSuccess(id, response, status) {
	if (status >= 200 && status <300) {
		window.location.replace("http://localhost:8080/success.html");
	} else {
		alert(response);
	}
}