function authorize(x, y) {
	console.log("authorize");
	var xhr = new XMLHttpRequest();
	xhr.open('POST', 'http://localhost:8080/oauth/token?client_id=web'
		+ '&client_secret=inventory&grant_type=password&username='+ x +'&password=' + y, true);
	xhr.onreadystatechange = function()
    {
        if (xhr.readyState == 4 && xhr.status == 200)
        {
            Classes = JSON.parse(xhr.responseText); // responseText -- текст ответа.
	  		console.log(Classes);
        }
    };
	xhr.send();
}