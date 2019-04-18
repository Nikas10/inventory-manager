function authorize(username, password) {
	console.log("authorize");
	var request = tokenRequest + 'client_id=' + clientId 
	+ '&client_secret=' + clientSecret + '&grant_type=password&username='
	+ username +'&password=' + password;
	processAjax("auth", 'POST', request, null, callbackTypesessionStorage);
}