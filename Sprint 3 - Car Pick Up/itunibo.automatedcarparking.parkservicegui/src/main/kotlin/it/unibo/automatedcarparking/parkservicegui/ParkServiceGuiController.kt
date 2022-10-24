package it.unibo.automatedcarparking.parkservicegui

import it.unibo.automatedcarparking.parkservicegui.qakutil.RemoteQakContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct


@Controller
class ParkServiceGuiController {

    @Autowired
    private lateinit var businessLogicConfig: BusinessLogicConfig

    @PostConstruct
    fun init() {
        val businessLogicContext = RemoteQakContext(businessLogicConfig.hostname, businessLogicConfig.port, "parkservicegui")
        ParkService.init(businessLogicContext)
    }

    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/")
    fun homePage(model: Model): String {
        println("------------------- TransportTrolleyController homePage $model")
        model.addAttribute("arg", appName)
        return "parkservicegui"
    }

    @PostMapping("/parking_car_interest")
    @ResponseBody
    fun parkingCarInterest(): String {
        println("parking car interest request")
        val slotnum = ParkService.enterIndoorArea()
        println("received slotnum: $slotnum")
        return "$slotnum"
    }

    @PostMapping("/car_enter")
    @ResponseBody
    fun carEnter(@RequestBody slotnumString : String): String {
        val slotnum = slotnumString.toInt()
        println("car enter request - slotnum : $slotnum")
        val tokenid = ParkService.enterParkingArea(slotnum)
        println("received tokenid: $tokenid")
        return tokenid
    }

    @PostMapping("/car_pick_up")
    @ResponseBody
    fun carPickUp(@RequestBody tokenid : String): String {
        println("car pick up request - tokenid : $tokenid")
        val response = ParkService.exitParkingArea(tokenid)
        println("car pick up response: $response")
        return response
    }

    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity(
            "BaseController ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }
}