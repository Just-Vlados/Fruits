window.onload = variables();
function setName() {
    var Name = document.getElementById("Name");
    if(Name.style.visibility=="hidden") {
        var InName = document.getElementById("myName");
        if(InName.value.trim()) {
            var SelEct = document.getElementById("select");
            Name.textContent = InName.value;
            InName.value="";
            InName.disabled = true;
            InName.style.visibility = "hidden";
            SelEct.style.visibility = "hidden";
            Name.style.visibility="visible";
            var tArea = document.getElementById("inMessage");
            tArea.readOnly = false;
        }
    }
    return;
}

function deleteName() {
	var Name = document.getElementById("Name");
    if(Name.style.visibility=="visible") {
        var InName = document.getElementById("myName");
        InName.disabled = false;
        InName.style.visibility = "visible";
        var SelEct = document.getElementById("select");
        SelEct.style.visibility = "visible";
        Name.textContent = "";
        Name.style.visibility="hidden";
        var tArea = document.getElementById("inMessage");
        tArea.value = "";
        tArea.readOnly = true;
    }
    return;
}

function sendMessage() {
	
	var c = document.getElementById("messages");
    var textArea = document.createElement('div');
	 
	textArea.id = 'sendmessage';
	var text = document.getElementById('inMessage');
	textArea.innerHTML = text.value;
    c.appendChild(textArea);
	text.value = '';
}
