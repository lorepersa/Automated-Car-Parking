package it.unibo.automatedcarparking.mockdatagui

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
class MockDataGuiController {

    @Autowired
    private lateinit var weightSensorConfig: WeightSensorConfig

    @Autowired
    private lateinit var businessLogicConfig: BusinessLogicConfig

    @Autowired
    private lateinit var outSonarConfig: OutSonarConfig

    @Autowired
    private lateinit var thermometerConfig: ThermometerConfig

    @PostConstruct
    fun init() {
        WeightSensor.init(weightSensorConfig)
        ParkingSlotsController.init(businessLogicConfig)
        OutSonar.init(outSonarConfig)
        Thermometer.init(thermometerConfig)
    }

    @Value("\${spring.application.name}")
    var appName: String? = null
    @GetMapping("/")
    fun homePage(model: Model): String {
        println("------------------- MockDataGui homePage $model")
        model.addAttribute("arg", appName)
        return "mockdatagui"
    }

    @PostMapping("/set_weight")
    @ResponseBody
    fun setWeight(@RequestBody weightString : String) : String {
        val weight = weightString.toInt()
        println("set weight $weight")
        WeightSensor.setWeight(weight)

        return ""
    }

    @PostMapping("/reset_parkingslots")
    @ResponseBody
    fun resetParkingSlots() : String {
        println("reset parking slots")
        ParkingSlotsController.reset()
        return ""
    }

    @PostMapping("/set_distance")
    @ResponseBody
    fun setDistance(@RequestBody distanceString : String) : String {
        val distance = distanceString.toInt()
        println("set distance $distance")
        OutSonar.setDistance(distance)

        return ""
    }

    @PostMapping("/set_temperature")
    @ResponseBody
    fun setTemperature(@RequestBody temperatureString : String) : String {
        val temperature = temperatureString.toInt()
        println("set temperature $temperature")
        Thermometer.setTemperature(temperature)

        return ""
    }
    
    @PostMapping("/get_outsonar_config")
    @ResponseBody
    fun getOutsonarConfig() : String {
        println("get_outsonar_config")
        if (OutSonar.isVirtual()) {
            return "virtual"
        } else {
            return "real"
        }
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
