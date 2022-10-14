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
        IndoorArea.init(businessLogicContext)
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
        val slotnum = IndoorArea.enterIndoorArea()
        println("received slotnum: $slotnum")
        return "$slotnum"
    }

    @PostMapping("/car_enter")
    @ResponseBody
    fun carEnter(@RequestBody slotnumString : String): String {
        val slotnum = slotnumString.toInt()
        println("car enter request - slotnum : $slotnum")
        val tokenid = IndoorArea.enterParkingArea(slotnum)
        println("received tokenid: $tokenid")
        return tokenid
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