function register() {
	console.log("register");
	var body = {};
	var request = apiAccount + '';
	processAjax("registration", 'POST', request, body, callbackTypeSessionStorage);
}