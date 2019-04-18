var clientId = "web";
var clientSecret = "inventory";
var localhost = 'http://localhost:8080';
var tokenRequest = localhost + '/oauth/token?';
var apiRequest = localhost + '/api';

var apiAccount = apiRequest + '/account';
var apiHolder = apiRequest + '/holder';
var apiRole = apiRequest + '/role';
var apiRequisition = apiRequest + '/requisition';
var apiInventory = apiRequest + '/inventory';

/*
requestType - 'POST', 'GET', 'PUT', 'DELETE', 'PATCH'
requestId - request identifier
request - compiled url
requestBody - JSON formatted body
callback - callback function after ajax to process result
Compile an URL and pass it here with the rest of parameters (type, body, ..)
*/
function processAjax(requestId, requestType, request, requestBody, callback) {
    console.log(request);
    console.log(JSON.stringify(requestBody));
	var xhr = new XMLHttpRequest();
	xhr.open(requestType, request, true);
    xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function()
    {
        if (xhr.readyState == 4)
        {
            callback(requestId, JSON.parse(xhr.responseText), xhr.status);
        }
    };
    if (requestBody != null && requestBody != undefined) {
    	xhr.send(JSON.stringify(requestBody));	
    } else {
    	xhr.send();
    }
	
}

/*
Saves request result to session storage under
id - request identifier and
response - request result, if
status - request http status - is in 200 group
*/
function callbackTypeSessionStorage(id, response, status) {
	if (status >= 200 && status <300) {
		sessionStorage.setItem(id, response);
	}
}