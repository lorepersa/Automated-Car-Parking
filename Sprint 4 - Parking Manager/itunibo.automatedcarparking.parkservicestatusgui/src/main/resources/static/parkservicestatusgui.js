//global Variables
var refreshWenvPressed = false;
var transportTrolleyStopped = false;
var fanOn = false;
var fanManual = false;
var weightSensorOn = false;
var outSonarOn = false;

var dtfreeAlarmRaised = false
var carEnterAlarmRaised = false
var temperatureAlarmRaised = false
var moveFailWENVAlarmRaised = false
var moveFailStartAlarmRaised = false
var fanFailureAlarmRaised = false

function getStompClient() {
    const socket = new SockJS('/ws');
    return Stomp.over(socket);
}

function connect() {

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            let automatedCarParkingStatus = JSON.parse(xhttp.responseText)
            indoorAreaStatusUpdate(automatedCarParkingStatus)
            outdoorAreaStatusUpdate(automatedCarParkingStatus)
            temperatureStatusUpdate(automatedCarParkingStatus)
            fanStatusUpdate(automatedCarParkingStatus)
            transportTrolleyStatusUpdate(automatedCarParkingStatus)
            parkingSlotsStatusUpdate(automatedCarParkingStatus)
        }

    }
    xhttp.open("POST", "/status", true)
    xhttp.send()

    indoorareaClient = getStompClient()
    indoorareaClient.connect({}, frame => {
        indoorareaClient.subscribe('/topic/indoorareastatus', function (update) {
            console.log(update)
            indoorAreaStatusUpdate(JSON.parse(update.body));
        })
    })

    outdoorareaClient = getStompClient()
    outdoorareaClient.connect({}, frame => {
        outdoorareaClient.subscribe('/topic/outdoorareastatus', function (update) {
            console.log(update)
            outdoorAreaStatusUpdate(JSON.parse(update.body));
        });
    })

    temperatureClient = getStompClient()
    temperatureClient.connect({}, frame => {
        temperatureClient.subscribe('/topic/temperaturestatus', function (update) {
            console.log(update)
            temperatureStatusUpdate(JSON.parse(update.body));
        });
    })

    fanClient = getStompClient()
    fanClient.connect({}, frame => {
        fanClient.subscribe('/topic/fanstatus', function (update) {
            console.log(update)
            fanStatusUpdate(JSON.parse(update.body));
        });
    })

    transportTrolleyClient = getStompClient()
    transportTrolleyClient.connect({}, frame => {
        transportTrolleyClient.subscribe('/topic/transporttrolleystatus', function (update) {
            console.log(update)
            transportTrolleyStatusUpdate(JSON.parse(update.body));
        });
    })

    parkingSlotsClient = getStompClient()
    parkingSlotsClient.connect({}, frame => {
        parkingSlotsClient.subscribe('/topic/parkingslotsstatus', function (update) {
            console.log(update)
            parkingSlotsStatusUpdate(JSON.parse(update.body));
        });

    });
}

function alarm(id, text, sound){
  var alarm_space = document.getElementById("alarm");
  if (id === null || id === undefined){
    console.log("Alarm() called without ID")
    //alarm_space.innerHTML += '<div class="error"><p>'+text+'</p></div>';
  } else {
    // get alarm element from given id
    var alarm = document.getElementById(id);
    // if exists a previous alarm with the same id, delete it
    if (alarm !== undefined && alarm !== null){
      alarm.remove();
    }
    // create the alarm
    alarm_space.innerHTML +=  '<div id="'+id+'" class="error"><p>'+text+'</p><span><button type="button" id="delete_'+id+'_button" onclick="remove_alarm(\''+id+'\')">X</button></span></div>';
    // onClick() event for delete_alarm_button
    // document.getElementById("delete_"+id+"_button").onclick = e => {
    //   if (document.getElementById(id) !== undefined && document.getElementById(id) !== null){
    //     document.getElementById(id).remove();
    //   }
    // }
    // Sound part
    if (sound){
      new Audio('./alarm_sound.wav').play()
    }
  }

}

function remove_alarm(id){
  if (document.getElementById(id) !== undefined && document.getElementById(id) !== null){
    document.getElementById(id).remove();
  }
}

function indoorAreaStatusUpdate(status){

  // json object fields:
  // status.weightsensorOn [BOOLEAN]
  // status.indoorAreaWeight [INT]
  // status.indoorAreaReserved [BOOLEAN]
  // status.indoorAreaEngaged [BOOLEAN]
  // status.indoorAreaCarEnterTimeoutAlarm [BOOLEAN]

  //get html elements
  var indoor_area = document.getElementById("indoor_area");
  var weight = document.getElementById("weight");
  var weight_sensor = document.getElementById("weight_sensor");

  //Handle indoor area reserved/engaged/free
  if (status.indoorAreaReserved){
    indoor_area.removeAttribute('class');
    indoor_area.classList.add("reserved");
  }

  if (status.indoorAreaEngaged){
    indoor_area.removeAttribute('class');
    indoor_area.classList.add("engaged");
  }

  if (!status.indoorAreaReserved && !status.indoorAreaEngaged) {
    indoor_area.removeAttribute('class');
    indoor_area.classList.add("free");
  }
  //Handle indoorAreaCarEnterTimeoutAlarm
  if (status.indoorAreaCarEnterTimeoutAlarm && !carEnterAlarmRaised){
    // write in alarm section that alarm has raised
    alarm('car_enter_timeout_alarm', 'The CAR ENTER timeout has expired!', true)
      carEnterAlarmRaised = true
  }

  if (!status.indoorAreaCarEnterTimeoutAlarm) {
      carEnterAlarmRaised = false
      remove_alarm('car_enter_timeout_alarm')
  }
  //Handle weight sensor observation
  if (status.weightsensorOn) {
      weightSensorOn = true
    //restore the opacity
    weight_sensor.style.opacity = '1';
    //write weight measured
    weight.innerHTML = status.indoorAreaWeight;
  } else {
      weightSensorOn = false
    //setting a minor opacity
    weight_sensor.style.opacity = '0.5';
  }
}

function outdoorAreaStatusUpdate(status){

  // json object fields:
  // status.outsonarOn [BOOLEAN]
  // status.outdoorAreaDistance [INT]
  // status.outdoorAreaReserved [BOOLEAN]
  // status.outdoorAreaEngaged [BOOLEAN]
  // status.outdoorAreaDTFREETimeoutAlarm [BOOLEAN]

  //get html elements
  var outdoor_area = document.getElementById("outdoor_area");
  var distance = document.getElementById("distance");
  out_sonar = document.getElementById("out_sonar");

  //Handle outdoor area reserved/engaged/free
  if (status.outdoorAreaReserved){
    outdoor_area.removeAttribute('class');
    outdoor_area.classList.add("reserved");
  }

  if (status.outdoorAreaEngaged){
    outdoor_area.removeAttribute('class');
    outdoor_area.classList.add("engaged");
  }

  if (!status.outdoorAreaReserved && !status.outdoorAreaEngaged) {
    outdoor_area.removeAttribute('class');
    outdoor_area.classList.add("free");
  }
  //Handle outdoorAreaDTFREETimeoutAlarm
  if (status.outdoorAreaDTFREETimeoutAlarm && !dtfreeAlarmRaised){
    // write in alarm section that alarm has raised
    alarm('dtfree_timeout_alarm', 'The DTFREE timeout has expired!', true)
      dtfreeAlarmRaised = true
  }

  if (!status.outdoorAreaDTFREETimeoutAlarm) {
      dtfreeAlarmRaised = false
      remove_alarm('dtfree_timeout_alarm')
  }
  //Handle out sonar observation
  if (status.outsonarOn) {
      outSonarOn = true
    //restore opacity
    out_sonar.style.opacity = '1';
    //write distance measured
    distance.innerHTML = status.outdoorAreaDistance;
  } else {
      outSonarOn = false
    //setting a minor opacity
    out_sonar.style.opacity = '0.5';
  }
}

function temperatureStatusUpdate(status){
  // json object fields:
  // status.parkingAreaTemperature [INT]
  // status.parkingAreaTemperatureHigh [BOOLEAN]

  // get html elements
  var temperature = document.getElementById("temperature");
  // write temperature measured
  temperature.innerHTML = status.parkingAreaTemperature;
  // handle temperature higher than TMAX
  if (status.parkingAreaTemperatureHigh){
    var sound_on = false;
    if (fanManual){
      sound_on = true;
    }
    // write in alarm section that temperature is high

      if (!temperatureAlarmRaised) {
          alarm('temperature_alarm', 'The temperature is high!', sound_on);
          temperatureAlarmRaised = true
      }
  } else {
      temperatureAlarmRaised = false
      remove_alarm('temperature_alarm')
  }

}

function getFanStatus(status) {
    let amof
    if (status.fanOn) {
        if(status.fanAutomatic){
          amof = "AUTOMATIC | ON"
        }else{
          amof = "MANUAL | ON"
        }
    } else {
        if (status.fanAutomatic) {
            amof = "AUTOMATIC | OFF"
        } else {
            amof = "MANUAL | OFF"
        }
    }
    return amof
}

function fanStatusUpdate(status){
  // json object fields:
  // status.fanOn [BOOLEAN]
  // status.fanAutomatic [BOOLEAN]
  // status.fanFailureReason [STRING]

  // get html elements
  var start_stop_button = document.getElementById("start_stop_fan_button");
  var manual_automatic_button = document.getElementById("manual_automatic_button");
  // handle fanOn fanOff
  if (status.fanOn){
    start_stop_button.innerHTML = "stop";
    fanOn = true;
  } else {
    start_stop_button.innerHTML = "start";
    fanOn = false;
  }
  //handle manual automatic mode
  if (status.fanAutomatic){
    manual_automatic_button.innerHTML = "manual";
    fanManual = false;
    start_stop_button.disabled = true;
  } else {
    manual_automatic_button.innerHTML = "automatic";
    fanManual = true;
    start_stop_button.disabled = false;
  }
  // write aut/man on/off status
  document.getElementById("fan_status").innerHTML = getFanStatus(status)
  // handle fan failure for some reason
  if (status.fanFailureReason !== "X" && !fanFailureAlarmRaised){
    // write in alarm section the fan failure reason
    alarm('fan_failure_alarm', "Fan failure: " + status.fanFailureReason, true);
    fanFailureAlarmRaised = true
  }

  if (status.fanFailureReason === "X") {
      fanFailureAlarmRaised = false
      remove_alarm('fan_failure_alarm')
  }
}

function getStatusIdleWorkingStopped(status) {
    let iws
    if (status.transportTrolleyStopped) {
        iws = "STOPPED"
    } else if(status.transportTrolleyIdle) {
        iws = "IDLE"
    } else {
        iws = "WORKING"
    }
    return iws
}

function transportTrolleyStatusUpdate(status) {

    // json object fields:
    // status.stopped [BOOLEAN]
    // status.moveFailed [BOOLEAN]
    // status.idle [BOOLEAN]
    // status.coordinate.column [INTEGER]
    // status.coordinate.row [INTEGER]
    // status.coordinate.direction [STRING] (TODO mmh non serve, da rivedere)

    // Start/Stop transport trolley button
    var start_stop_button = document.getElementById("start_stop_transport_trolley_button");

    /* ________________________________
        START/STOP & IDLE/WORKING/STOPPED
       ________________________________*/
    if (status.transportTrolleyStopped) {
      if (status.transportTrolleyMoveFailed && !refreshWenvPressed) {
          start_stop_button.disabled = true
      } else {
          start_stop_button.disabled = false;
      }
      start_stop_button.innerHTML = "start";
      transportTrolleyStopped = true;
    } else {
      start_stop_button.innerHTML = "stop";
      transportTrolleyStopped = false;
    }

    document.getElementById("status_iws").innerHTML = getStatusIdleWorkingStopped(status)

    /* ________________________________
        MOVE FAILED
       ________________________________*/
    if (status.transportTrolleyMoveFailed && !refreshWenvPressed) {
        console.log("SONO DENTRO MOVE FAILED")
        document.getElementById("refresh_wenv_button").disabled = false;
        if (!moveFailWENVAlarmRaised) {
            moveFailWENVAlarmRaised = true
            alarm('move_failed_alarm', 'Warning! Move fail detected! Click on the "REFRESH WENV" button to put the transport trolley at HOME.', true);
        }
    } else {
        document.getElementById("refresh_wenv_button").disabled = true;
    }

    if (!status.transportTrolleyMoveFailed) {
        moveFailWENVAlarmRaised = false
        moveFailStartAlarmRaised = false
        remove_alarm('move_failed_alarm')
        remove_alarm('refresh_wenv_alarm')
    }

    /* ________________________________
        POSITION
       ________________________________*/
    document.getElementById("transport_trolley_position").innerHTML = "(" + status.transportTrolleyCoordinate.column + "," + status.transportTrolleyCoordinate.row + ")";
    // add transport trolley icon to the map
    if ( document.getElementsByClassName("robot")[0] !== undefined ) {
      // remove the transport trolley icon from the previous cell
      document.getElementsByClassName("robot")[0].classList.remove("robot");
    }
    document.getElementById("cell_"+status.transportTrolleyCoordinate.column+"_"+status.transportTrolleyCoordinate.row).classList.add("robot");

}

function parkingSlotsStatusUpdate(status){
  // json object fields:
  // status.parkingSlotsStatus [SET]
  //possible status: FREE, ENGAGED, RESERVED

  var parkingslots = status.parkingSlotsStatus
  var map_order = [ '2_1', '2_2', '2_3', '3_1', '3_2', '3_3' ];

  for (var i=0; i<6; i++){
    //if parking_slot oocupied{
    document.getElementById("cell_"+map_order[parkingslots[i].number - 1]).className = " parking_slot";
    if (parkingslots[i].status.status === "ENGAGED"){
      document.getElementById("cell_"+map_order[parkingslots[i].number - 1]).className += " engaged";
    } else if (parkingslots[i].status.status === "RESERVED"){
      document.getElementById("cell_"+map_order[parkingslots[i].number - 1]).className += " reserved";
    }
  }
}

function setWEnvUrl() {

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            let url = xhttp.responseText
            document.getElementById("wenv").src = url
        }
    }
    xhttp.open("POST", "/wenv_url", true)
    xhttp.send()
}

function init_map() {
  //body reference
  var position = document.getElementById("robot_position");
  // create elements <table> and a <tbody>
  var tbl = document.createElement("table");
  tbl.setAttribute("id", "map");
  tbl.setAttribute("class", "map");
  var tblBody = document.createElement("tbody");
  // cells creation
  for (var j = 0; j < 7; j++) {
    // table row creation
    var row = document.createElement("tr");
    if (j === 0 || j === 6){
      // create indoor/outdoor cells
      var cell = document.createElement("td");
      cell.setAttribute("colspan", "6");
      cell.classList.add("td_null");
      row.appendChild(cell);
      cell = document.createElement("td");
      if (j === 0){
        cell.setAttribute("id", "indoor_area");
      } else {
        cell.setAttribute("id", "outdoor_area");
      }
      cell.classList.add("free");
      row.appendChild(cell);
    } else {
      // create other cells
      for (var i = 0; i < 7; i++) {
        // create map cells
        var cell = document.createElement("td");
        id = "cell_"+i+"_"+(j-1)
        cell.setAttribute("id", id);
        // add cell to row
        row.appendChild(cell);
      }
    }

    //row added to end of table body
    tblBody.appendChild(row);
  }

  // append the <tbody> inside the <table>
  tbl.appendChild(tblBody);
  // put <table> in the <body>
  position.appendChild(tbl);

  // Set 6 Parking slots
  document.getElementById("cell_2_1").classList.add("parking_slot");
  document.getElementById("cell_3_1").classList.add("parking_slot");
  document.getElementById("cell_2_2").classList.add("parking_slot");
  document.getElementById("cell_3_2").classList.add("parking_slot");
  document.getElementById("cell_2_3").classList.add("parking_slot");
  document.getElementById("cell_3_3").classList.add("parking_slot");

}

function init_buttons(){
  // Variables for Buttons
  //Transport Trolley
  var refresh_wenv_button = document.getElementById("refresh_wenv_button");
  var start_stop_transport_trolley_button = document.getElementById("start_stop_transport_trolley_button");
  // Fan
  var manual_automatic_button = document.getElementById("manual_automatic_button");
  var start_stop_fan_button = document.getElementById("start_stop_fan_button");
  // Sensors
  var turn_on_off_weight_sensor_button =  document.getElementById("turn_on_off_weight_sensor_button");
  var turn_on_off_out_sonar_button =  document.getElementById("turn_on_off_out_sonar_button");

  // Start/Stop Transport Trolley Button
  start_stop_transport_trolley_button.onclick = e => {
    e.preventDefault()
    console.log("start/stop transport trolley pressed!")

    if (transportTrolleyStopped){
      //send request to start transport_trolley
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/manager_transport_trolley_start", true)
      xhttp.send()
      refreshWenvPressed = false
    } else {
      //send request to stop transport_trolley
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/manager_transport_trolley_stop", true)
      xhttp.send()
    }
  }

  // Refresh Wenv Button
  refresh_wenv_button.onclick = e => {
    e.preventDefault()
    console.log("refresh wenv pressed!")
    document.getElementById("wenv").src += ''
    //delete move_failed_alarm if yet exists
      remove_alarm('move_failed_alarm')
    // refresh_wenv_alarm
      if (!moveFailStartAlarmRaised) {
          moveFailStartAlarmRaised = true
          alarm('refresh_wenv_alarm', 'Now click on the "START" button to let the Transport Trolley complete its task!', true);
      }
      // buttons logic
    refresh_wenv_button.disabled = true
    refreshWenvPressed = true
    start_stop_transport_trolley_button.disabled = false
  }

  // Start/Stop Fan
  start_stop_fan_button.onclick = e => {
    e.preventDefault()
    console.log("start/stop fan pressed!")

    if (fanOn){
      //send request to stop fan
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/manager_fan_off", true)
      xhttp.send()
    } else {
      //send request to start fan
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/manager_fan_on", true)
      xhttp.send()
    }
  }

  manual_automatic_button.onclick = e => {
    e.preventDefault()
    console.log("manual/automatic fan pressed!")

    if (fanManual){
      //send request to start transport_trolley
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/manager_fan_automatic_mode", true)
      xhttp.send()
    } else {
      //send request to stop transport_trolley
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/manager_fan_manual_mode", true)
      xhttp.send()
    }
  }

  // On/Off sensors button
  turn_on_off_weight_sensor_button.onclick = e => {
    e.preventDefault()
    console.log("weight sensor on/off pressed!")

    if (weightSensorOn){
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/weightsensor_info_off", true)
      xhttp.send()
    } else {
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/weightsensor_info_on", true)
      xhttp.send()
    }


  }
  turn_on_off_out_sonar_button.onclick = e => {
    e.preventDefault()
    console.log("out sonar on/off pressed!")

    if (outSonarOn){
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/outsonar_info_off", true)
      xhttp.send()
    } else {
      const xhttp = new XMLHttpRequest()
      xhttp.open("POST", "/outsonar_info_on", true)
      xhttp.send()
    }
  }
}

function div_sensor_actuators_same_height(){
  const main = document.getElementById('actuator_fan').offsetHeight;
  document.getElementById("actuator_transport_trolley").style.height = document.getElementById('actuator_fan').style.height = document.getElementById('sensors').style.height = main + 'px';
}

function init(){
  connect()
  setWEnvUrl()
  init_map();
  init_buttons();

  //style functions
  div_sensor_actuators_same_height();

}

window.onload = e => {
  init();
}
