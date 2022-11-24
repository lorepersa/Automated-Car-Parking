package it.unibo.automatedcarparking.parkservicestatusgui

import it.unibo.automatedcarparking.parkservicestatusgui.qakutil.RemoteQakContext
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
import it.unibo.automatedcarparking.parkservicestatusgui.status.*


@Controller
class ParkServiceStatusGuiController : IAutomatedCarParkingStatusObserver {

    @Autowired
    private lateinit var template : SimpMessagingTemplate
    @Autowired
    private lateinit var businessLogicConfig: BusinessLogicConfig
    @Autowired
    private lateinit var wenvConfig: WEnvConfig

    private lateinit var indoorAreaStatus: IndoorAreaStatus
    private lateinit var outdoorAreaStatus: OutdoorAreaStatus
    private lateinit var temperatureStatus: TemperatureStatus
    private lateinit var fanStatus: FanStatus
    private lateinit var transportTrolleyStatus: TransportTrolleyStatus
    private lateinit var parkingSlotsStatus: ParkingSlotsStatus

    @PostConstruct
    fun init() {
        val businessLogicContext = RemoteQakContext(businessLogicConfig.hostname, businessLogicConfig.port, "parkservicestatusgui")
        Manager.init(businessLogicContext)
        Manager.registerObserver(this)
    }

    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/")
    fun homePage(model: Model): String {
        println("------------------- ParkServiceStatusGuiController homePage $model")
        model.addAttribute("arg", appName)
        return "parkservicestatusgui"
    }

    @PostMapping("/manager_fan_on")
    @ResponseBody
    fun fanOn(): String {
        println("request - fan on")
        Manager.fanOn()
        return ""
    }

    @PostMapping("/manager_fan_off")
    @ResponseBody
    fun fanOff(): String {
        println("request - fan off")
        Manager.fanOff()
        return ""
    }

    @PostMapping("/manager_fan_manual_mode")
    @ResponseBody
    fun fanManualMode(): String {
        println("request - fan manual mode")
        Manager.fanManualMode()
        return ""
    }

    @PostMapping("/manager_fan_automatic_mode")
    @ResponseBody
    fun fanAutomaticMode(): String {
        println("request - fan automatic mode")
        Manager.fanAutomaticMode()
        return ""
    }

    @PostMapping("/manager_transport_trolley_start")
    @ResponseBody
    fun transportTrolleyStart(): String {
        println("request - transport trolley start")
        Manager.transportTrolleyStart()
        return ""
    }

    @PostMapping("/manager_transport_trolley_stop")
    @ResponseBody
    fun transportTrolleyStop(): String {
        println("request - transport trolley stop")
        Manager.transportTrolleyStop()
        return ""
    }

    @PostMapping("/weightsensor_info_on")
    @ResponseBody
    fun weightSensorInfoOn(): String {
        println("request - weightsensor info on")
        Manager.weightSensorInfoOn()
        return ""
    }

    @PostMapping("/weightsensor_info_off")
    @ResponseBody
    fun weightSensorInfoOff(): String {
        println("request - weightsensor info off")
        Manager.weightSensorInfoOff()
        return ""
    }

    @PostMapping("/outsonar_info_on")
    @ResponseBody
    fun outSonarInfoOn(): String {
        println("request - outsonar info on")
        Manager.outSonarInfoOn()
        return ""
    }

    @PostMapping("/outsonar_info_off")
    @ResponseBody
    fun outSonarInfoOff(): String {
        println("request - outsonar info off")
        Manager.outSonarInfoOff()
        return ""
    }

    @PostMapping("/wenv_url")
    @ResponseBody
    fun sendWEnvUrl(): String {
        println("send WEnv url")
        return wenvConfig.url
    }

    @PostMapping("/status")
    @ResponseBody
    fun getStatus(): String {
        println("requested status")
        return Manager.getLatestStatusJson()
    }

    @ExceptionHandler
    fun handle(ex: Exception): ResponseEntity<*> {
        val responseHeaders = HttpHeaders()
        return ResponseEntity(
            "BaseController ERROR ${ex.message}",
            responseHeaders, HttpStatus.CREATED
        )
    }

    override fun update(status: AutomatedCarParkingStatus) {
        val newIndoorAreaStatus = IndoorAreaStatus(status)
        if (!this::indoorAreaStatus.isInitialized
            || indoorAreaStatus != newIndoorAreaStatus) {
            indoorAreaStatus = newIndoorAreaStatus
            template.convertAndSend("/topic/indoorareastatus", indoorAreaStatus)
        }

        val newOutdoorAreaStatus = OutdoorAreaStatus(status)
        if (!this::outdoorAreaStatus.isInitialized
            || outdoorAreaStatus != newOutdoorAreaStatus) {
            outdoorAreaStatus = newOutdoorAreaStatus
            template.convertAndSend("/topic/outdoorareastatus", outdoorAreaStatus)
        }

        val newTemperatureStatus = TemperatureStatus(status)
        if (!this::temperatureStatus.isInitialized
            || temperatureStatus != newTemperatureStatus) {
            temperatureStatus = newTemperatureStatus
            template.convertAndSend("/topic/temperaturestatus", temperatureStatus)
        }

        val newFanStatus = FanStatus(status)
        if (!this::fanStatus.isInitialized
            || fanStatus != newFanStatus) {
            fanStatus = newFanStatus
            template.convertAndSend("/topic/fanstatus", fanStatus)
        }

        val newTransportTrolleyStatus = TransportTrolleyStatus(status)
        if (!this::transportTrolleyStatus.isInitialized
            || transportTrolleyStatus != newTransportTrolleyStatus) {
            transportTrolleyStatus = newTransportTrolleyStatus
            template.convertAndSend("/topic/transporttrolleystatus", transportTrolleyStatus)
        }

        val newParkingSlotsStatus = ParkingSlotsStatus(status)
        if (!this::parkingSlotsStatus.isInitialized
            || parkingSlotsStatus != newParkingSlotsStatus) {
            parkingSlotsStatus = newParkingSlotsStatus
            template.convertAndSend("/topic/parkingslotsstatus", parkingSlotsStatus)
        }
    }
}
