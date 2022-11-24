function hideSection(section){
  document.getElementById(section).style.opacity = "0"
  document.getElementById(section).classList.add('hidden')
}
function showSection(section){
  document.getElementById(section).classList.remove('hidden')
  document.getElementById(section).style.opacity = "1"
}

function handleOutsonarConfigResponse(response) {
    if (response === "virtual") {
        // show slider
        showSection("outsonar-mock")
    } else {
        // hide slider
        hideSection("outsonar-mock")
    }
}

window.onload = e => {
    const config_sonar_xhttp = new XMLHttpRequest()
    config_sonar_xhttp.onreadystatechange = function() {
        if (config_sonar_xhttp.readyState === 4 && config_sonar_xhttp.status === 200)
            handleOutsonarConfigResponse(config_sonar_xhttp.responseText)
    }
    config_sonar_xhttp.open("POST", "/get_outsonar_config", true)
    config_sonar_xhttp.send()


    var weightsensor = document.getElementById("weightsensor");
    var output_weightsensor = document.getElementById("weightsensor_value");
    output_weightsensor.innerHTML = weightsensor.value; // Display the default slider value

    // Update the current weightsensor value (each time you drag the slider handle)
    weightsensor.oninput = function () {
        output_weightsensor.innerHTML = this.value;
    }

    // send dispatch to weightsensor
    weightsensor.onchange = function() {
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/set_weight", true)
        xhttp.send(this.value)
    }

    var outsonar = document.getElementById("outsonar");
    var output_outsonar = document.getElementById("outsonar_value");
    output_outsonar.innerHTML = outsonar.value; // Display the default slider value

    // Update the current outsonar value (each time you drag the slider handle)
    outsonar.oninput = function () {
        output_outsonar.innerHTML = this.value;
    }

    // send dispatch to outsonar
    outsonar.onchange = function() {
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/set_distance", true)
        xhttp.send(this.value)
    }

    var resetParkingSlotsButton = document.getElementById("reset_parkingslots_button")

    resetParkingSlotsButton.onclick = e => {
        e.preventDefault()
        /* SEND REQUEST */
        const xhttp = new XMLHttpRequest()
        xhttp.onreadystatechange = function() {}
        xhttp.open("POST", "/reset_parkingslots", true)
        xhttp.send()
    }

    var thermometer = document.getElementById("thermometer");
    var output_thermometer = document.getElementById("thermometer_value");
    output_thermometer.innerHTML = thermometer.value; // Display the default slider value

    // Update the current thermometer value (each time you drag the slider handle)
    thermometer.oninput = function () {
        output_thermometer.innerHTML = this.value;
    }

    // send dispatch to thermometer
    thermometer.onchange = function() {
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/set_temperature", true)
        xhttp.send(this.value)
    }
}
