package it.unibo.first_problem_analysis.test


import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.junit.Test
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Before
import kotlin.test.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.qakutils.CoapQAK
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.After

class AppTest {

	companion object {
		var coapQakWeightSensor: CoapQAK
		var coapQakOutSonar: CoapQAK
		var coapQakThermometer: CoapQAK
		var coapQakBusinessLogic: CoapQAK

		var receivedSlotnum = 0
		var indoorAreaOccupied = false
		var receivedTokens = mutableListOf<String>()
		var outdoorAreaOccupied = false


		init {
			coapQakWeightSensor = CoapQAK("127.0.0.1", 8060, "ctxweightsensor", "weightsensor")
			coapQakOutSonar = CoapQAK("127.0.0.1", 8061, "ctxoutsonar", "outsonar")
			coapQakThermometer = CoapQAK("127.0.0.1", 8062, "ctxthermometer", "thermometer")
			coapQakBusinessLogic = CoapQAK("127.0.0.1", 8065, "ctxbusinesslogic", "businesslogic")
		}


		@AfterClass
		@JvmStatic
		fun destroy() {
			coapQakWeightSensor.stop()
			coapQakOutSonar.stop()
			coapQakThermometer.stop()
			coapQakBusinessLogic.stop()
		}
	}


	suspend fun requestToEnterInIndoorArea(testName: String): Boolean {
		delay(1000)
		var slotnumMessage = coapQakBusinessLogic.syncRequest(testName, "parking_car_interest", "X")

		receivedSlotnum = slotnumMessage.getPayload().toInt()

		return receivedSlotnum != 0
	}

	suspend fun enterInIndoorArea(testName: String): Boolean {

		if (receivedSlotnum > 0) {
			delay(2000)
			coapQakWeightSensor.sendDispatch(testName, "input_weight", "1000")
			indoorAreaOccupied = true
			delay(2000)

			return true
		}

		return false
	}

	suspend fun enterInParkingArea(testName: String): Boolean {
		// when the tokenid is received -> the transportTrolley is at INDOOR
		delay(1000)
		if (receivedSlotnum > 0) {
			var tokenid = coapQakBusinessLogic.syncRequest(testName, "car_enter", "$receivedSlotnum")

			if (!tokenid.getPayload().equals("INVALID", true)) {
				assertTrue(receivedTokens.add(tokenid.getPayload()))
				delay(500)
				coapQakWeightSensor.sendDispatch(testName, "input_weight", "0")
				indoorAreaOccupied = false
				receivedSlotnum = 0
				delay(2000)
				return true
			}
		}


		return false
	}

	suspend fun exitFromParkingArea(testName: String): Boolean {

		val tokenid = receivedTokens.removeFirstOrNull()

		tokenid?.let {
			delay(1000)
			var response = coapQakBusinessLogic.syncRequest(testName, "car_pickup", tokenid)

			if (response.getName().equals("accept_out_success")) {
				delay(1000)
				coapQakOutSonar.sendDispatch(testName, "input_distance", "10")
				outdoorAreaOccupied = true
				delay(3000)
				return true
			}
		}

		return false
	}

	suspend fun exitFromOutdoorArea(testName: String): Boolean {

		if (outdoorAreaOccupied) {
			delay(1000)
			coapQakOutSonar.sendDispatch(testName, "input_distance", "200")
			outdoorAreaOccupied = false
			delay(2000)
			return true
		}

		return false

	}
	
	@Before
	fun setup() {
		runBlocking {
			println("BEGIN SETUP")
			
			coapQakBusinessLogic.sendDispatch("setup", "manager_fan_manual_mode", "X")
			delay(300)
			coapQakBusinessLogic.syncRequest("setup", "manager_fan_off", "X")
			delay(300)
			coapQakBusinessLogic.sendDispatch("setup", "manager_transport_trolley_start", "X")
			delay(300)
			coapQakThermometer.sendDispatch("setup", "input_temperature", "22")
			delay(300)
			coapQakWeightSensor.sendDispatch("setup", "input_weight", "0")
			delay(300)
			coapQakOutSonar.sendDispatch("setup", "input_distance", "200")
			
			delay(2000)
			
			println("END SETUP")
		}
	}

	@After
	fun teardown() {

		runBlocking {
			println("BEGIN TEARDOWN")

			delay(1000)

			if (outdoorAreaOccupied) {
				println("[setup] outdoor area occupied...")
				exitFromOutdoorArea("setup")
				delay(2000)
				outdoorAreaOccupied = false
			}

			while (exitFromParkingArea("setup")) {
				println("[setup] exit from parking area...")
				delay(2000)
				exitFromOutdoorArea("setup")
				delay(2000)
			}

			if (indoorAreaOccupied) {
				println("[setup] indoor area occupied...")
				enterInParkingArea("setup")
				delay(5000)
				exitFromParkingArea("setup")
				delay(2000)
				exitFromOutdoorArea("setup")
				indoorAreaOccupied = false
			}

			delay(2000)

			println("END TEARDOWN")
		}


	}

	class BusinessLogicStatus {
		var indoor_area_weight = 0
		var indoor_area_free = true
		var outdoor_area_distance = 0
		var outdoor_area_free = true
		var outdoor_alarm_on = false
		var temperature = 0
		var temperature_high = false
		var fan_automatic = false
		var fan_on = false
		var transport_trolley_stopped = false
		var transport_trolley_working = false
		var free_parking_slot = Array<Boolean>(6) { true }
	}

	fun parseBusinessLogicStatus(status: String): BusinessLogicStatus {
		val blstatus = BusinessLogicStatus()
		val tokens = status.substringAfter("(").split("|")

		val indoor_tokens = tokens.get(0).substringAfter("(").substringBefore(")").split(",")
		blstatus.indoor_area_weight = indoor_tokens.get(0).toInt()
		blstatus.indoor_area_free = indoor_tokens.get(1).equals("FREE", true)

		val outdoor_tokens = tokens.get(1).substringAfter("(").substringBefore(")").split(",")
		blstatus.outdoor_area_distance = outdoor_tokens.get(0).toInt()
		blstatus.outdoor_area_free = outdoor_tokens.get(1).equals("FREE", true)

		val temperature_tokens = tokens.get(2).substringAfter("(").substringBefore(")").split(",")
		blstatus.temperature = temperature_tokens.get(0).toInt()
		blstatus.temperature_high = temperature_tokens.get(1).equals("HIGH", true)

		val fan_tokens = tokens.get(3).substringAfter("(").substringBefore(")").split(",")
		blstatus.fan_automatic = fan_tokens.get(0).equals("AUTOMATIC", true)
		blstatus.fan_on = fan_tokens.get(1).equals("ON", true)

		val transport_trolley_tokens = tokens.get(4).substringAfter("(").substringBefore(")")
		blstatus.transport_trolley_working = transport_trolley_tokens.equals("WORKING", true)
		blstatus.transport_trolley_stopped = transport_trolley_tokens.equals("STOPPED", true)

		val parking_slots_tokens = tokens.get(5).substringAfter("(").substringBefore(")").split(",")

		for (i in 0 until blstatus.free_parking_slot.size) {
			blstatus.free_parking_slot[i] = parking_slots_tokens.get(i).equals("FREE", true)
		}

		val outdoor_alarm_token = tokens.get(6).substringAfter("(").substringBefore(")")
		blstatus.outdoor_alarm_on = outdoor_alarm_token.equals("ON", true)

		return blstatus
	}


	suspend fun normalCarEnterParkingArea(testName: String): Boolean {

		if (requestToEnterInIndoorArea(testName))
			if (enterInIndoorArea(testName))
				return enterInParkingArea(testName)

		return false
	}

	suspend fun normalCarExitParkingArea(testName: String): Boolean {

		if (exitFromParkingArea(testName))
			return exitFromOutdoorArea(testName)

		return false
	}

	@Test
	fun TestSuccessAcceptIn() {
		runBlocking {
			val testName = "TestSuccessAcceptIn"
			println("RUN TEST: $testName")
			println("Request to enter in indoor area...")
			assertTrue(requestToEnterInIndoorArea(testName))
		}
	}

	@Test
	fun TestTripOneCar() {
		runBlocking {
			val testName = "TestTripOneCar"
			println("RUN TEST: $testName")

			println("Full enter in parking area...")
			assertTrue(normalCarEnterParkingArea(testName))
			delay(8000)
			println("Full exit from parking area...")
			assertTrue(normalCarExitParkingArea(testName))
		}
	}

	@Test
	fun TestFullParkingArea() {
		runBlocking {
			val testName = "TestFullParkingArea"
			println("RUN TEST: $testName")

			for (i in 1..6) {
				println("Request to enter in parking area...")
				assertTrue(normalCarEnterParkingArea(testName))
			}
			delay(5000)
			println("Request to enter in parking area [should fail]...")
			assertFalse(normalCarEnterParkingArea(testName))
		}
	}

	@Test
	fun TestInvalidTokenID() {
		runBlocking {
			val testName = "TestInvalidTokenID"
			println("RUN TEST: $testName")

			println("Full enter in parking area...")
			assertTrue(normalCarEnterParkingArea(testName))
			val invalid_tokenid = "PMSa2"

			delay(4000)

			println("Request to exit from parking area with invalid token...")
			val response = coapQakBusinessLogic.syncRequest(testName, "car_pickup", invalid_tokenid)
			assertFalse(response.getName().equals("accept_out_success"))
		}
	}

	@Test
	fun TestOccupiedIndoorArea() {
		runBlocking {
			val testName = "TestOccupiedIndoorArea"
			println("RUN TEST: $testName")

			println("[first car] Request to enter in indoor area...")
			assertTrue(requestToEnterInIndoorArea(testName))
			println("[first car] Entering indoor area...")
			assertTrue(enterInIndoorArea(testName))
			// now indoor area is occupied

			coroutineScope {
				val second_car = async {
					println("[second car] Request to enter indoor area...")
					assertTrue(requestToEnterInIndoorArea(testName))
				}
				val first_car = async {
					println("[first car] Wait some time before enter parking area...")
					delay(6000)

					assertTrue(second_car.isActive) // response not get after some time
					println("[first car] Entering parking area...")
					assertTrue(enterInParkingArea(testName))
				}
			}
		}
	}

	@Test
	fun TestOccupiedOutdoorArea() {
		runBlocking {
			val testName = "TestOccupiedOutdoorArea"
			println("RUN TEST: $testName")

			println("[first car] full enter parking area...")
			assertTrue(normalCarEnterParkingArea(testName))
			println("[second car] full enter parking area...")
			assertTrue(normalCarEnterParkingArea(testName))

			println("[first car] exit from parking area...")
			assertTrue(exitFromParkingArea(testName))

			coroutineScope {
				val second_car = async {
					println("[second car car] exit from parking area...")
					assertTrue(exitFromParkingArea(testName))
				}
				val first_car = async {
					println("[first car] wait some time in outdoor area...")
					delay(12000)

					assertTrue(second_car.isActive) // response not get after some time
					println("[first car] exit from outdoor area...")
					assertTrue(exitFromOutdoorArea(testName))
				}
			}
		}
	}

	@Test
	fun TestTransportTrolleyBackToHome() {
		runBlocking {
			val testName = "TestTransportTrolleyBackToHome"
			println("RUN TEST: $testName")

			println("full enter parking area...")
			assertTrue(normalCarEnterParkingArea(testName))

			var at_home = false

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(at_home, "Transport trolley not returned at HOME")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						at_home = !blstatus.transport_trolley_working && !blstatus.transport_trolley_stopped
					} while (!at_home)

					watchdog.cancel()
				}

			}
		}
	}

	@Test
	fun TestWeightSensorDetectCar() {

		runBlocking {
			val testName = "TestWeightSensorDetectCar"
			println("RUN TEST: $testName")

			coapQakWeightSensor.sendDispatch(testName, "input_weight", "850")
			var car_detected = false

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(car_detected, "Car not detected by logical weight sensor")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						car_detected = !blstatus.indoor_area_free
					} while (!car_detected)

					watchdog.cancel()
				}

			}
		}
	}

	@Test
	fun TestWeightSensorCarNotDetected() {
		runBlocking {
			val testName = "TestWeightSensorCarNotDetected"
			println("RUN TEST: $testName")

			coapQakWeightSensor.sendDispatch(testName, "input_weight", "850")
			delay(4000)
			var car_not_detected = false

			coapQakWeightSensor.sendDispatch(testName, "input_weight", "0")

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(car_not_detected, "Car detected by logical weight sensor")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						car_not_detected = blstatus.indoor_area_free
					} while (!car_not_detected)

					watchdog.cancel()
				}

			}

		}
	}

	@Test
	fun TestSonarDetectCar() {
		runBlocking {
			val testName = "TestSonarDetectCar"
			println("RUN TEST: $testName")

			coapQakOutSonar.sendDispatch(testName, "input_distance", "10")

			var car_detected = false

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(car_detected, "Car not detected by logical sonar")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						car_detected = !blstatus.outdoor_area_free
					} while (!car_detected)

					watchdog.cancel()
				}

			}

		}
	}

	@Test
	fun TestSonarCarNotDetected() {
		val testName = "TestSonarCarNotDetected"
		println("RUN TEST: $testName")

		runBlocking {
			coapQakOutSonar.sendDispatch(testName, "input_distance", "10")

			delay(4000)
			var car_not_detected = false

			coapQakOutSonar.sendDispatch(testName, "input_distance", "200")

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(car_not_detected, "Car detected by logical sonar")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						car_not_detected = blstatus.outdoor_area_free
					} while (!car_not_detected)

					watchdog.cancel()
				}

			}
		}
	}

	@Test
	fun TestThermometerTemperatureHigh() {
		runBlocking {
			val testName = "TestThermometerTemperatureHigh"
			println("RUN TEST: $testName")

			coapQakThermometer.sendDispatch(testName, "input_temperature", "38")

			var temperature_high = false

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(temperature_high, "Temperature low")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						temperature_high = blstatus.temperature_high
					} while (!temperature_high)

					watchdog.cancel()
				}
			}
		}
	}

	@Test
	fun TestThermometerTemperatureLow() {
		runBlocking {
			val testName = "TestThermometerTemperatureLow"
			println("RUN TEST: $testName")

			coapQakThermometer.sendDispatch(testName, "input_temperature", "38")

			delay(4000)
			var temperatature_low = false

			coapQakThermometer.sendDispatch(testName, "input_temperature", "31")

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(temperatature_low, "Temperature high")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						temperatature_low = !blstatus.temperature_high
					} while (!temperatature_low)

					watchdog.cancel()
				}
			}

		}
	}

	@Test
	fun TestParkServiceStatusGuiSetFanManualMode() {
		val testName = "TestFanManualMode"
		runBlocking {
			println("RUN TEST: $testName")

			coapQakBusinessLogic.sendDispatch(testName, "manager_fan_manual_mode", "X")

			// check manual mode

			coroutineScope {

				var manual_mode = false;


				var watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(manual_mode, "Fan automatic mode")
				}
				var controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						manual_mode = !blstatus.fan_automatic
					} while (!manual_mode)

					watchdog.cancel()
				}
			}
		}

		runBlocking {

			coapQakBusinessLogic.syncRequest(testName, "manager_fan_on", "X")

			coroutineScope {


				// check fan on
				var fan_on = false

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(fan_on, "Fan off")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						fan_on = blstatus.fan_on
					} while (!fan_on)

					watchdog.cancel()
				}
			}
		}

		runBlocking {

			coapQakBusinessLogic.syncRequest(testName, "manager_fan_off", "X")

			// check fan off
			var fan_off = false

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(fan_off, "Fan on")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						fan_off = !blstatus.fan_on
					} while (!fan_off)

					watchdog.cancel()
				}

			}

		}
	}


	@Test
	fun TestParkServiceStatusGuiSetFanAutomaticMode() {

		val testName = "TestFanManualMode"
		runBlocking {

			println("RUN TEST: $testName")

			coapQakBusinessLogic.sendDispatch(testName, "manager_fan_automatic_mode", "X")

			// check automatic mode

			var automatic_mode = false

			coroutineScope {

				var watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(automatic_mode, "Fan manual mode")
				}
				var controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						automatic_mode = blstatus.fan_automatic
					} while (!automatic_mode)

					watchdog.cancel()
				}

			}
		}

		runBlocking {

			coapQakThermometer.sendDispatch(testName, "input_temperature", "38")



			coroutineScope {
				// check fan on
				var fan_on = false

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(fan_on, "Fan off")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						fan_on = blstatus.fan_on
					} while (!fan_on)

					watchdog.cancel()
				}
			}
		}

		runBlocking {

			coapQakThermometer.sendDispatch(testName, "input_temperature", "32")

			coroutineScope {

				// check fan off
				var fan_off = false

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(fan_off, "Fan on")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						fan_off = !blstatus.fan_on
					} while (!fan_off)

					watchdog.cancel()
				}

			}

		}

	}

	@Test
	fun TestParkServiceStatusGuiResumeTransportTrolley() {
		val testName = "TestParkServiceStatusGuiResumeTransportTrolley"
		runBlocking {

			println("RUN TEST: $testName")

			coapQakBusinessLogic.sendDispatch(testName, "manager_transport_trolley_stop", "X")
			var transport_trolley_stopped = false

			coroutineScope {

				var watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(transport_trolley_stopped, "Transport trolley not stopped")
				}
				var controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						transport_trolley_stopped = blstatus.transport_trolley_stopped
					} while (!transport_trolley_stopped)

					watchdog.cancel()
				}

			}
		}
		runBlocking {

			coapQakBusinessLogic.sendDispatch(testName, "manager_transport_trolley_start", "X")
			var transport_trolley_started = false

			coroutineScope {

				val watchdog = async {
					delay(35000)
					// after 35 sec check at_home
					assertTrue(transport_trolley_started, "Transport trolley stopped")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						transport_trolley_started = !blstatus.transport_trolley_stopped
					} while (!transport_trolley_started)

					watchdog.cancel()
				}

			}
		}

	}

	@Test
	fun TestDtfreeAlarm() {
		runBlocking {
			val testName = "TestDtfreeAlarm"
			println("RUN TEST: $testName")

			assertTrue(normalCarEnterParkingArea(testName))
			delay(4000)

			assertTrue(exitFromParkingArea(testName))

			var alarm_on = false

			coroutineScope {

				val watchdog = async {
					delay(120000)
					// after 35 sec check at_home
					assertTrue(alarm_on, "Alarm not fired")
				}
				val controller = async {
					do {
						delay(1000)
						val resource = coapQakBusinessLogic.readResource()
						val blstatus = parseBusinessLogicStatus(resource)
						//println(resource)
						alarm_on = blstatus.outdoor_alarm_on
					} while (!alarm_on)

					watchdog.cancel()
				}

			}
		}
	}

}

