
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

function getStatusIdleWorkingStopped(status) {
    let iws

    if (status.stopped) {
        iws = "STOPPED"
    } else if(status.idle) {
        iws = "IDLE"
    } else {
        iws = "WORKING"
    }

    return iws
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
      document.getElementById("stop").disabled = true;
    } else {
      document.getElementById("start").disabled = true;
      document.getElementById("stop").disabled = false;
    }

    document.getElementById("status_iws").innerHTML = getStatusIdleWorkingStopped(status)

    if (status.moveFailed && !refreshWenvPressed) {
        console.log("SONO DENTRO MOVE FAILED")
        document.getElementById("refresh-wenv").disabled = false;
        document.getElementById("move-fail").innerHTML = '<div class="error"><p>Attenzione! Move fail rilevata! Riavvia il wenv premendo il pulsante REFRESH WENV</p></div>'
    } else {
        document.getElementById("refresh-wenv").disabled = true;
    }

    document.getElementById("status_position").innerHTML = "(" + status.coordinate.column + "," + status.coordinate.row + ")"
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
    document.getElementById("stop").onclick = e => {
        e.preventDefault()
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/transport_trolley_stop", true)
        xhttp.send()
    }
    document.getElementById("refresh-wenv").onclick = e => {
        e.preventDefault()
        document.getElementById("wenv").src += ''
        document.getElementById("move-fail").innerHTML = '<div class="error"><p>WEnv riavviato! Premi ora il pulsante START per riavviare il transport trolley!</p></div>'
        document.getElementById("refresh-wenv").disabled = true
        refreshWenvPressed = true
        document.getElementById("start").disabled = false
    }
    document.getElementById("car-park").onclick = e => {
        e.preventDefault()
        const slotnum = document.getElementById("slotnum").value
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/car_park", true)
        xhttp.send(slotnum)
    }
    document.getElementById("car-pick-up").onclick = e => {
        e.preventDefault()
        const slotnum = document.getElementById("slotnum").value
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/car_pick_up", true)
        xhttp.send(slotnum)
    }
    document.getElementById("sleep").onclick = e => {
        e.preventDefault()
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/transport_trolley_sleep", true)
        xhttp.send()
    }
}
