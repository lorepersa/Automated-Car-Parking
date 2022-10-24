
let refreshWenvPressed = false


function connect() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200)
            statusUpdate(JSON.parse(xhttp.responseText))
    }
    xhttp.open("POST", "/status", true)
    xhttp.send()


    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        stompClient.subscribe('/topic/transporttrolleystatus', function (update) {
            console.log(update)
            statusUpdate(JSON.parse(update.body));
        });
    });
}

function statusUpdate(status) {

    // json object fields:
    // status.stopped [BOOLEAN]
    // status.moveFailed [BOOLEAN]
    // status.idle [BOOLEAN]
    // status.coordinate.column [INTEGER]
    // status.coordinate.row [INTEGER]
    // status.coordinate.direction [STRING] (TODO mmh non serve, da rivedere)

    if (status.stopped) {
        if (status.moveFailed && !refreshWenvPressed) {
            document.getElementById("start").disabled = true
        } else {
            document.getElementById("start").disabled = false;
        }
    } else {
      document.getElementById("start").disabled = true;
    }


    if (status.moveFailed && !refreshWenvPressed) {
        console.log("SONO DENTRO MOVE FAILED")
        document.getElementById("refresh-wenv").disabled = false;
        document.getElementById("move-fail").innerHTML = '<div class="error"><p>Warning! Move fail detected! Click on the "REFRESH WENV" button to put the transport trolley at HOME.</p></div>'
    } else {
        document.getElementById("refresh-wenv").disabled = true;
    }

}

function setWEnvUrl() {

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            let url = xhttp.responseText
            document.getElementById("wenv").src = url
        }

    }
    xhttp.open("POST", "/wenv_url", true)
    xhttp.send()
}


window.onload = e => {
    connect()
    setWEnvUrl()
    document.getElementById("start").onclick = e => {
        e.preventDefault()
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/transport_trolley_start", true)
        xhttp.send()
        document.getElementById("move-fail").innerHTML = ''
        refreshWenvPressed = false
    }
    document.getElementById("refresh-wenv").onclick = e => {
        e.preventDefault()
        document.getElementById("wenv").src += ''
        document.getElementById("move-fail").innerHTML = '<div class="error"><p>Now click on the "START" button to let the Transport Trolley complete its task!</p></div>'
        document.getElementById("refresh-wenv").disabled = true
        refreshWenvPressed = true
        document.getElementById("start").disabled = false
    }
}
