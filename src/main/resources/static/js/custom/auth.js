function authorize() {
	console.log("authorize");
	var username = document.getElementById("authLogin").value;
	var password = document.getElementById("authPassword").value;
	var request = tokenRequest + 'client_id=' + clientId 
	+ '&client_secret=' + clientSecret + '&grant_type=password&username='
	+ username +'&password=' + password;
	processAjax("auth", 'POST', request, null, callbackTypeAuthCookie);
}

function callbackTypeAuthCookie(id, response, status) {
	if (status >= 200 && status <300) {
		console.log(JSON.stringify(response));
		setCookie(id, response.access_token, response.expires_in);
		processSecuredAjax('currentUser', 'GET', apiAccount, null, callbackTypeStorageAndHome);
	} else {
		alert(response);
	}
}

function callbackTypeStorageAndHome(id, response, status) {
	if (status >= 200 && status <300) {
		console.log(JSON.stringify(response));
		sessionStorage.setItem(id, JSON.stringify(response));
		window.location.replace("http://localhost:8080/home.html");
	}
}