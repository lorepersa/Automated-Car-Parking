package it.unibo.automatedcarparking.sprint1.gui

import it.unibo.automatedcarparking.sprint1.gui.status.TransportTrolleyStatus
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct

@Controller
class TransportTrolleyController : ITransportTrolleyObserver {

    @Autowired
    private lateinit var template : SimpMessagingTemplate

    @Autowired
    private lateinit var businessLogicConfig: BusinessLogicConfig

    @Autowired
    private lateinit var wenvConfig: WEnvConfig

    @PostConstruct
    fun init() {
        TransportTrolley.init(businessLogicConfig)
        TransportTrolley.registerObserver(this)
    }

    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/")
    fun homePage(model: Model): String {
        println("------------------- TransportTrolleyController homePage $model")
        model.addAttribute("arg", appName)
        return "gui"
    }

    @PostMapping("/wenv_url")
    @ResponseBody
    fun sendWEnvUrl(): String {
        println("send WEnv url")
        return wenvConfig.url
    }

    @PostMapping("/transport_trolley_start")
    @ResponseBody
    fun sendStartToTransportTrolley(): String {
        println("send start to transport trolley")
        runBlocking {
            TransportTrolley.transportTrolleyStart()
        }
        return ""
    }

    @PostMapping("/transport_trolley_stop")
    @ResponseBody
    fun sendStopToTransportTrolley(): String {
        println("send stop to transport trolley")
        runBlocking {
            TransportTrolley.transportTrolleyStop()
        }
        return ""
    }

    @PostMapping("/transport_trolley_sleep")
    @ResponseBody
    fun doSleepMode(): String {
        println("sleep mode request")
        runBlocking {
            TransportTrolley.sleep()
        }
        return ""
    }

    @PostMapping("/car_park")
    @ResponseBody
    fun doCarPark(@RequestBody body: String): String {
        println("car park request")
        runBlocking {
            try {
                TransportTrolley.doCarPark(body.toInt())
            } catch (e: Exception) {
                println("error not integer")
                e.printStackTrace()
            }
        }
        return ""
    }

    @PostMapping("/car_pick_up")
    @ResponseBody
    fun doCarPickUp(@RequestBody body: String): String {
        println("car pick up request")
        runBlocking {
            try {
                TransportTrolley.doCarPickUp(body.toInt())
            } catch (e: Exception) {
                println("error not integer")
            }
        }
        return ""
    }

    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity(
                "BaseController ERROR ${ex.message}",
                responseHeaders, HttpStatus.CREATED
        )
    }


    override fun update(status: TransportTrolleyStatus) {
        println("received update: $status")
        template.convertAndSend("/topic/transporttrolleystatus", status)
    }


    @PostMapping("/status")
    @ResponseBody
    fun getStatus(): String {
        println("requested status")
        return TransportTrolley.getLatestStatusJson()
    }
}