var messageList = [];
var userName = '';

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random()/1000000;

	return Math.floor(date * random).toString();
};

var theMessage = function(text,name) {
	return {
		username: name,
		message: text,
		id: uniqueId()
	};
};

var appState = {
	mainUrl : 'http://localhost:999/chat',
	messageList:[],
	token : 'TE11EN'
};

function run() {
	var dialogPanel = document.getElementsByClassName('centerblock')[0];
	dialogPanel.addEventListener('click', delegateEvent);
	restore();
}


function createAllMessages(allMessages) 
{
	if(allMessages == null)
		return;
	for(var i = 0; i < allMessages.length; i++)
		addTodoInternal(allMessages[i]);
	
}

function delegateEvent(evtObj) {
		if(evtObj.type === 'click' && evtObj.target.classList.contains('btn-primary'))
			onSendMessageButtonClick(userName);
		if(evtObj.type === 'ondblclick' && evtObj.target.classList.contains('sent-message-area')) {
            var cng = evtObj.target.parentElement;
            if(evtObj.target.id === '1') {
                //onCngItem(cng);
            }
            else
                onDelItem(cng);
        }
		if(evtObj.type === 'click' && evtObj.target.classList.contains('sent-message-area')) {
            enter(evtObj);
        }
}

function enter(evtObj) {
        var messList = appState.messageList; 
        var k = evtObj.target.parentNode;
        var text = evtObj.target.value;
        k.removeChild(evtObj.target);
        k.appendChild(document.createTextNode(text));
        for(var i = 0; i < messList.length; i++) {
            if(messList[i].id !== k.id)
                continue;
            messList[i].description = evtObj.target.value;
            put(appState.mainUrl + '?id=' + messList[i].id, JSON.stringify(messList[i]), function() {
            });
            return;
        }    
}

function onSendMessageButton()
{
	onSendMessageButtonClick(userName);
}

function onSendMessageButtonClick(userName) {
	if(userName == '')
		userName = "Name";
	var textMessage = document.getElementById('inMessage');
	var newMessage = theMessage(textMessage.value, userName);
	if (textMessage.value == '')
		return;
	textMessage.value = '';
	addTodo(newMessage);
}

function addTodo(task) {
	post (appState.mainUrl, JSON.stringify(task), function(){
		restore();
	});
}
function addTodoInternal(message) {
	var item = createMessage(message);
	var items = document.getElementsByClassName('sent')[0];
	var messageList = appState.messageList;

	messageList.push(message);
	items.appendChild(item);
	
}

function createMessage(message) {
	var temp = document.createElement('div');
	var htmlAsText = '<div class="sent-message-area" mes-id="идентификатор">user name: Message text </div><br>';
	temp.innerHTML = htmlAsText;
	update(temp.firstChild, message);
	return temp.firstChild;
}

function update(divItem, message) {
	divItem.setAttribute('mes-id', message.id);
	divItem.lastChild.textContent = message.username + ": " + message.message;
}
function onDelItem(divItem) {
	var messList = appState.messageList;

	for(var i = 0; i < messList.length; i++) {
		if(messList[i].id !== divItem.id)
			continue;
                    
        if(userName !== messList[i].name)
            return;
                
		toggle(messList[i], function() {
		});

		return;
	}
}

function toggle(mess, continueWith) {
	mess.deleted = !mess.deleted;
	del(appState.mainUrl + '?id=' + mess.id, JSON.stringify(mess), function() {});
}

function restore(continueWith) {
	var url = appState.mainUrl + '?token=' + appState.token;

	get(url, function(responseText) {
		console.assert(responseText != null);

		var response = JSON.parse(responseText);
		//alert(response.messages);

		appState.token = response.token;
		createAllMessages(response.messages);
		
		continueWith && continueWith();
	});
}


function changeName() {
	var newName = document.getElementById('myName');
	userName = newName.value;
	newName.value = '';
	var chatName = document.getElementById('chat-name');
	chatName.innerHTML = userName;
	var avatarName = document.getElementById('My-Name');
	avatarName.innerHTML = userName;
	
}

function changeStatusServerConnect() {
	var status = document.getElementById('server-connect');
	status.innerHTML = 'offline';
	
}

function get(url, continueWith, continueWithError) {
	ajax('GET', url, null, continueWith, continueWithError);
}

function post(url, data, continueWith, continueWithError) {
	ajax('POST', url, data, continueWith, continueWithError);	
}

function put(url, data, continueWith, continueWithError) {
	ajax('PUT', url, data, continueWith, continueWithError);	
}

function del(url, data, continueWith, continueWithError) {
	ajax('DELETE', url, data, continueWith, continueWithError);	
}

function isError(text) {
	if(text == "")
		return false;
	
	try {
		var obj = JSON.parse(text);
	} catch(ex) {
		changeStatusServerConnect();
		return true;
	}

	return !!obj.error;
}
function defaultErrorHandler(message) {
	console.error(message);
	changeStatusServerConnect();
}
function mesError(){
	document.getElementById('parent_popup').style.display='block';
}

function ajax(method, url, data, continueWith, continueWithError) {
	var xhr = new XMLHttpRequest();

	continueWithError = continueWithError || defaultErrorHandler;
	xhr.open(method || 'GET', url, true);

	xhr.onload = function () {
		if (xhr.readyState !== 4)
			return;

		if(xhr.status != 200) {
			continueWithError('Error on the server side, response ' + xhr.status);
			changeStatusServerConnect();
			mesError();
			return;
		}

		if(isError(xhr.responseText)) {
			continueWithError('Error on the server side, response ' + xhr.responseText);
			changeStatusServerConnect();
			mesError();
			return;
		}

		continueWith(xhr.responseText);
	};    

    xhr.ontimeout = function () {
    	ontinueWithError('Server timed out !');
		changeStatusServerConnect();
		mesError();
    }

    xhr.onerror = function (e) {
		changeStatusServerConnect();
		mesError();
        continueWithError(errMsg);
    };

    xhr.send(data);
}