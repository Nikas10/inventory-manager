var clientId = "web";
var clientSecret = "inventory";
var localhost = 'http://localhost:8080';
var tokenRequest = localhost + '/oauth/token?';
var apiRequest = localhost + '/api';

var apiAccount = apiRequest + '/account';
var apiHolder = apiRequest + '/holder';
var apiRole = apiRequest + '/role';
var apiRequisition = apiRequest + '/requisition';
var apiRole = apiInventory + '/inventory';

/*
requestType - 'POST', 'GET', 'PUT', 'DELETE', 'PATCH'
requestId - request identifier
request - compiled url
requestBody - JSON formatted body
callback - callback function after ajax to process result
Compile an URL and pass it here with the rest of parameters (type, body, ..)
*/
function processAjax(requestId, requestType, request, requestBody, callback) {
	var xhr = new XMLHttpRequest();
	xhr.open(requestType, request, true);
	xhr.onreadystatechange = function()
    {
        if (xhr.readyState == 4)
        {
            callback(JSON.parse(xhr.responseText), xhr.status);
        }
    };
    if (requestBody != null && requestBody != undefined) {
    	xhr.send(requestBody);	
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