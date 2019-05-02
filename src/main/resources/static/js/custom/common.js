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

function processSecuredAjax(requestId, requestType, request, requestBody, callback) {
    console.log(request);
    console.log(JSON.stringify(requestBody));
    var xhr = new XMLHttpRequest();
    xhr.open(requestType, request, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    var credential = getCookie("auth");
    if (credential) {
        xhr.setRequestHeader("Authorization", "Bearer " + credential);
    }
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

/*Cookies*/
function setCookie(name, value, millis) {
    var expires = "";
    if (millis) {
        var date = new Date();
        date.setTime(date.getTime() + (millis));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {   
    document.cookie = name+'=; Max-Age=-99999999;';  
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

function renderNavbar() {
    var auth = getCookie("auth");
    if (auth) {
        var userdata = JSON.parse(sessionStorage.getItem("currentUser"));
        document.getElementById('navbar-right-side').appendChild(createUserNavbarElement(userdata));        
    }
}

function createUserNavbarElement(userdata) {
    //root
    var li = document.createElement("li");
    li.setAttribute('class', 'nav-item dropdown');
    //text
    var t = document.createTextNode(userdata.login);
    var a = document.createElement("a");
    a.setAttribute('class', 'nav-link dropdown-toggle');
    a.setAttribute('href', '#');
    a.setAttribute('id', 'navbarDropdown');
    a.setAttribute('role', 'button');
    a.setAttribute('data-toggle', 'dropdown');
    a.setAttribute('aria-haspopup', 'true');
    a.setAttribute('aria-expanded', 'false');
    a.appendChild(t);    
    
    //menu
    var d = document.createElement("div");
    d.setAttribute('class', 'dropdown-menu');
    d.setAttribute('aria-labelledby', 'navbarDropdown');
    //profile
    var ad = document.createElement("a");
    var tad = document.createTextNode('Profile');
    ad.setAttribute('class', 'dropdown-item');
    ad.setAttribute('href', 'profile.html');
    ad.appendChild(tad);
    d.appendChild(ad);
    //divider
    var dd = document.createElement("div");
    dd.setAttribute('class', 'dropdown-divider');
    d.appendChild(dd);
    //exit
    var add = document.createElement("a");
    var tadd = document.createTextNode('Exit');
    add.setAttribute('class', 'dropdown-item');
    add.setAttribute('href', '#');
    add.appendChild(tadd);
    d.appendChild(add);    
    //append here
    li.appendChild(a);
    li.appendChild(d);

    return li;
}