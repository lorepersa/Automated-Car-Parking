

window.onload = e => {
    var slider = document.getElementById("weightsensor");
    var output = document.getElementById("demo");
    output.innerHTML = slider.value; // Display the default slider value

    // Update the current slider value (each time you drag the slider handle)
    slider.oninput = function () {
        output.innerHTML = this.value;
    }

    // send dispatch to weightsensor
    slider.onchange = function() {
        const xhttp = new XMLHttpRequest()
        xhttp.open("POST", "/set_weight", true)
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
}