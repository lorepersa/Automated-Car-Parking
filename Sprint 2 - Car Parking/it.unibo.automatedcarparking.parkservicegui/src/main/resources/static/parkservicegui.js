
function hideSection(section){
  document.getElementById(section).style.opacity = "0"
  document.getElementById(section).classList.add('hidden')
}
function showSection(section){
  document.getElementById(section).classList.remove('hidden')
  document.getElementById(section).style.opacity = "1"
}

function loader(){
  let speed = 30;
  let scale = 0.75; // Image scale (I work on 1080p monitor)
  let canvas;
  let ctx;
  let logoColor;

  let robot = {
      x: 200,
      y: 300,
      xspeed: 10,
      yspeed: 10,
      img: new Image()
  };

  (function main(){
      canvas = document.getElementById("loader");
      ctx = canvas.getContext("2d");
      robot.img.src = 'transport_trolley_1.png';

      //Draw the "tv screen"
      canvas.width  = window.innerWidth;
      canvas.height = window.innerHeight;

      pickColor();
      update();

  })();

  function update() {
      setTimeout(() => {
          //Draw the canvas background
          ctx.fillStyle = '#fff';
          ctx.fillRect(0, 0, canvas.width, canvas.height);
          //Draw robot Logo and his background
          ctx.fillStyle = logoColor;
          ctx.fillRect(robot.x, robot.y, robot.img.width*scale, robot.img.height*scale);
          ctx.drawImage(robot.img, robot.x, robot.y, robot.img.width*scale, robot.img.height*scale);
          //Move the logo
          robot.x+=robot.xspeed;
          robot.y+=robot.yspeed;
          //Check for collision
          checkHitBox();
          update();

      }, speed)
  }

  //Check for border collision
  function checkHitBox(){
      if(robot.x+robot.img.width*scale >= canvas.width || robot.x <= 0){
          robot.xspeed *= -1;
          pickColor();
      }

      if(robot.y+robot.img.height*scale >= canvas.height || robot.y <= 0){
          robot.yspeed *= -1;
          pickColor();
      }
  }

  //Pick a random color in RGB format
  function pickColor(){
      r = Math.random() * (254 - 0) + 0;
      g = Math.random() * (254 - 0) + 0;
      b = Math.random() * (254 - 0) + 0;

      logoColor = 'rgb('+r+','+g+', '+b+')';
  }
}

function persistTokenID(tokenid) {
    localStorage.setItem("tokenid", tokenid)
}

function persistSlotnum(slotnum) {
    localStorage.setItem("slotnum", slotnum)
}

function persistCheckpoint(checkpoint) {
    localStorage.setItem("checkpoint", checkpoint)
}

function handleParkingCarInterestResponse(slotnum) {
  hideSection("loader_section")
  if (slotnum === "0") {
    showSection("retry_section")
  } else {
    document.getElementById("slotnum").innerHTML = slotnum
    showSection("car_enter_section")
  }
    persistSlotnum(slotnum)
  persistCheckpoint("received_slotnum")
}

function handleCarEnterResponse(tokenid) {
    hideSection("loader_section")
    if (tokenid === "IndoorAreaNotReserved") {
        // error
        showSection("retry_section")
        persistCheckpoint("home")
    } else if (tokenid === "IndoorAreaNotEngagedByCar") {
        // retry
        showSection("car_enter_section")
    } else if (tokenid === "InvalidSlotnum") {
        // error
        showSection("retry_section")
        persistCheckpoint("home")
    } else {
        // success
        document.getElementById("tokenid").innerHTML = tokenid
        showSection("tokenid_section")
        persistTokenID(tokenid)
        persistCheckpoint("received_tokenid")
    }
}

function onInit() {
    let slotnum = localStorage.getItem("slotnum")
    let tokenid = localStorage.getItem("tokenid")
    let checkpoint = localStorage.getItem("checkpoint")

    if (checkpoint && checkpoint === "received_slotnum") {
        handleParkingCarInterestResponse(slotnum)
    } else if (checkpoint && checkpoint === "received_tokenid") {
        handleCarEnterResponse(tokenid)
    } else {
        showSection("park_interest_section")
    }
}

window.onload = e => {
    loader()
    onInit()

    document.getElementById("park_interest").onclick = e => {
        e.preventDefault()
        /* SEND REQUEST */
        const xhttp = new XMLHttpRequest()
        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200)
                handleParkingCarInterestResponse(xhttp.responseText)
        }
        xhttp.open("POST", "/parking_car_interest", true)
        xhttp.send()
        /* SHOW THE LOADER */
        hideSection("park_interest_section")
        showSection("loader_section")
    }
    document.getElementById("car_enter").onclick = e => {
        e.preventDefault()
        /* SEND REQUEST */
        const xhttp = new XMLHttpRequest()
        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200)
                handleCarEnterResponse(xhttp.responseText)
        }
        xhttp.open("POST", "/car_enter", true)
        xhttp.send(document.getElementById("slotnum").innerHTML)

        /* SHOW THE LOADER */
        hideSection("car_enter_section")
        showSection("loader_section")
    }

    document.getElementById("retry_back_home").onclick = e => {
        e.preventDefault()
        persistCheckpoint("home")
        window.location.href = window.location.href
    }

    document.getElementById("tokenid_back_home").onclick = e => {
        e.preventDefault()
        persistCheckpoint("home")
        window.location.href = window.location.href
    }

}
