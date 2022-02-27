package it.unibo.first_problem_analysis.test


import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.junit.*
import kotlin.test.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.qakutils.CoapQAKFactory
import it.unibo.qakutils.CoapQAK
import kotlinx.coroutines.async

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class AppTest {
	
	companion object {
			
		val coapQakFactory = CoapQAKFactory("localhost", 8053, "ctxtestablemodel")
		var coapQakPhysicalWeightSensor : CoapQAK
		var coapQakLogicalWeightSensor : CoapQAK
		var coapQakPhysicalSonar : CoapQAK
		var coapQakLogicalSonar : CoapQAK
		var coapQakPhysicalThermometer : CoapQAK
		var coapQakLogicalThermometer : CoapQAK
		var coapQakLogicalFan : CoapQAK
		var coapQakLogicalTransportTrolley : CoapQAK
		var coapQakBusinessLogic : CoapQAK
		var coapQakParkServiceGUI : CoapQAK
		var coapQakParkServiceStatusGUI : CoapQAK
		
		init {
			GlobalScope.launch {
				it.unibo.ctxTestableModel.main()
			}
			
			Thread.sleep(5000)
		
			coapQakPhysicalWeightSensor = coapQakFactory.getCoapQAK("physicalweightsensor")
			coapQakLogicalWeightSensor = coapQakFactory.getCoapQAK("logicalweightsensor")
			coapQakPhysicalSonar = coapQakFactory.getCoapQAK("physicalsonar")
			coapQakLogicalSonar = coapQakFactory.getCoapQAK("logicalsonar")
			coapQakPhysicalThermometer = coapQakFactory.getCoapQAK("physicalthermometer")
			coapQakLogicalThermometer = coapQakFactory.getCoapQAK("logicalthermometer")
			coapQakLogicalFan = coapQakFactory.getCoapQAK("logicalfan")
			coapQakLogicalTransportTrolley = coapQakFactory.getCoapQAK("logicaltransporttrolley")
			coapQakBusinessLogic = coapQakFactory.getCoapQAK("businesslogic")
			coapQakParkServiceGUI = coapQakFactory.getCoapQAK("parkservicegui")
			coapQakParkServiceStatusGUI = coapQakFactory.getCoapQAK("parkservicestatusgui")
		}

		
		@AfterClass @JvmStatic fun destroy() {
			coapQakPhysicalWeightSensor.stop()
			coapQakLogicalWeightSensor.stop()
			coapQakPhysicalSonar.stop()
			coapQakLogicalSonar.stop()
			coapQakPhysicalThermometer.stop()
			coapQakLogicalThermometer.stop()
			coapQakLogicalFan.stop()
			coapQakLogicalTransportTrolley.stop()
			coapQakBusinessLogic.stop()
			coapQakParkServiceGUI.stop()
			coapQakParkServiceStatusGUI.stop()
		}
	}
	
	
    @Before fun setup() {
		
		println("BEGIN SETUP")
		Thread.sleep(1000)
		coapQakPhysicalWeightSensor.setup()
		Thread.sleep(1000)
		coapQakLogicalWeightSensor.setup()
		Thread.sleep(1000)
		coapQakPhysicalSonar.setup()
		Thread.sleep(1000)
		coapQakLogicalSonar.setup()
		Thread.sleep(1000)
		coapQakPhysicalThermometer.setup()
		Thread.sleep(1000)
		coapQakLogicalThermometer.setup()
		Thread.sleep(1000)
		coapQakLogicalFan.setup()
		Thread.sleep(1000)
		coapQakLogicalTransportTrolley.setup()
		Thread.sleep(1000)
		coapQakBusinessLogic.setup()
		Thread.sleep(1000)
		coapQakParkServiceGUI.setup()
		Thread.sleep(1000)
		coapQakParkServiceStatusGUI.setup()
		Thread.sleep(6000)
		println("END SETUP")
	}

	
	fun normalCarEnterParkingArea(testName : String) : String {
		var slotnum = coapQakParkServiceGUI.syncRequest(testName, "req_client_accept_in", "X")
		
		if (slotnum.getPayload().toInt() > 0) {
			coapQakPhysicalWeightSensor.sendDispatch(testName, "input_weight", "1000.0")
			// when the tokenid is received -> the transportTrolley is at INDOOR
			var tokenid = coapQakParkServiceGUI.syncRequest(testName, "req_client_car_enter", slotnum.getPayload())
			coapQakPhysicalWeightSensor.sendDispatch(testName, "input_weight", "0.0")
			return tokenid.getPayload()
		}
		
		return ""
	}
	
	fun normalCarExitParkingArea(testName : String, tokenid : String) : Boolean {
		var response = coapQakParkServiceGUI.syncRequest(testName, "req_client_accept_out", tokenid)
		
		if (response.getName().equals("rep_client_accept_out_success")) {
			coapQakPhysicalSonar.sendDispatch(testName, "input_distance", "15.0")
			Thread.sleep(3000)
			coapQakPhysicalWeightSensor.sendDispatch(testName, "input_weight", "0.0")
			return true
		} 
		
		return false
	}

    @Test fun TestSuccessAcceptIn() {
		val testName = "TestSuccessAcceptIn"
		val response = coapQakParkServiceGUI.syncRequest(testName, "req_client_accept_in", "X")
		
		assertTrue(response.isSuccess(), "Error response")
		
		if (response.getName().equals("rep_client_accept_in")) {
			assertTrue(response.getPayload().toInt() > 0, "Expected SLOTNUM greater than 0")
		} else {
			fail("response incorrect")
		}
    }
	
	@Test fun TestTripOneCar() {
		val testName = "TestTripOneCar"
		val tokenid = normalCarEnterParkingArea(testName)
		Thread.sleep(10000)
		
		val ok = normalCarExitParkingArea(testName, tokenid)
		assertTrue(ok)
	}
	
	@Test fun TestFullParkingArea() {
		val testName = "TestFullParkingArea"
		var tokenid = normalCarEnterParkingArea(testName)
		assertNotEquals(tokenid, "", "error")
		tokenid = normalCarEnterParkingArea(testName)
		assertNotEquals(tokenid, "", "error")
		tokenid = normalCarEnterParkingArea(testName)
		assertNotEquals(tokenid, "", "error")
		tokenid = normalCarEnterParkingArea(testName)
		assertNotEquals(tokenid, "", "error")
		tokenid = normalCarEnterParkingArea(testName)
		assertNotEquals(tokenid, "", "error")
		tokenid = normalCarEnterParkingArea(testName)
		assertNotEquals(tokenid, "", "error")
		var response = coapQakParkServiceGUI.syncRequest(testName, "req_client_accept_in", "X")
		assertTrue(response.getPayload().toInt() == 0)
	}
	
	@Test fun TestInvalidTokenID() {
		val testName = "TestInvalidTokenID"
		normalCarEnterParkingArea(testName)
		val invalid_tokenid = "PMSa2"
		Thread.sleep(4000)
		assertFalse(normalCarExitParkingArea(testName, invalid_tokenid))
	}
	
	@Test fun TestOccupiedIndoorArea() {
		val testName = "TestOccupiedIndoorArea"
		
		coapQakParkServiceGUI.syncRequest(testName, "req_client_accept_in", "X")
		
		// now indoor area is occupied
		
		// wait for 15 seconds
		val result = coapQakParkServiceGUI.timedRequest(testName, "req_client_accept_in", "X", 15000)
		assertFalse(result.isSuccess())
		
	}
	
	@Test fun TestOccupiedOutdoorArea() {
		val testName = "TestOccupiedOutdoorArea"
		
		var tokenid1 = normalCarEnterParkingArea(testName)
		var tokenid2 = normalCarEnterParkingArea(testName)
		
		val response = coapQakParkServiceGUI.syncRequest(testName, "req_client_accept_out", tokenid1)
		if (response.getName().equals("rep_client_accept_out_success")) {
			coapQakPhysicalSonar.sendDispatch(testName, "input_distance", "15.0")
		}
		
		// now outdoor area is occupied
		
		// wait for 15 seconds
		val result = coapQakParkServiceGUI.timedRequest(testName, "req_client_accept_out", tokenid2, 15000)

		assertFalse(result.isSuccess())
	}
	
	@Test fun TestTransportTrolleyBackToHome() {
		val testName = "TestTransportTrolleyBackToHome"
		
		normalCarEnterParkingArea(testName)
		
		runBlocking {
			var at_home = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(at_home, "Transport trolley not returned at HOME")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalTransportTrolley.readResource()
					//println(resource)
					at_home = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("home", true)
				} while(!at_home)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestWeightSensorDetectCar() {
		val testName = "TestWeightSensorDetectCar"
		
		coapQakPhysicalWeightSensor.sendDispatch(testName, "input_weight", "850.0")
		runBlocking {
			var car_detected = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(car_detected, "Car not detected by logical weight sensor")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalWeightSensor.readResource()
					//println(resource)
					car_detected = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("occupied", true)
				} while(!car_detected)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestWeightSensorCarNotDetected() {
		val testName = "TestWeightSensorCarNotDetected"
		
		coapQakPhysicalWeightSensor.sendDispatch(testName, "input_weight", "850.0")
		runBlocking {
			delay(4000)
			var car_not_detected = false
			
			coapQakPhysicalWeightSensor.sendDispatch(testName, "input_weight", "0.0")
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(car_not_detected, "Car detected by logical weight sensor")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalWeightSensor.readResource()
					//println(resource)
					car_not_detected = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("free", true)
				} while(!car_not_detected)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestSonarDetectCar() {
		val testName = "TestSonarDetectCar"
		
		coapQakPhysicalSonar.sendDispatch(testName, "input_distance", "10.0")
		runBlocking {
			var car_detected = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(car_detected, "Car not detected by logical sonar")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalSonar.readResource()
					//println(resource)
					car_detected = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("occupied", true)
				} while(!car_detected)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestSonarCarNotDetected() {
		val testName = "TestSonarCarNotDetected"
		
		coapQakPhysicalSonar.sendDispatch(testName, "input_distance", "10.0")
		runBlocking {
			delay(4000)
			var car_not_detected = false
			
			coapQakPhysicalSonar.sendDispatch(testName, "input_distance", "100.0")
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(car_not_detected, "Car detected by logical sonar")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalSonar.readResource()
					println(resource)
					car_not_detected = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("free", true)
				} while(!car_not_detected)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestThermometerTemperatureHigh() {
		val testName = "TestThermometerTemperatureHigh"
		
		coapQakPhysicalThermometer.sendDispatch(testName, "input_temperature", "38.0")
		runBlocking {
			var temperature_high = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(temperature_high, "Temperature low")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalThermometer.readResource()
					//println(resource)
					temperature_high = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("high", true)
				} while(!temperature_high)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestThermometerTemperatureLow() {
		val testName = "TestThermometerTemperatureLow"
		
		coapQakPhysicalThermometer.sendDispatch(testName, "input_temperature", "38.0")
		runBlocking {
			delay(4000)
			var temperatature_low = false
			
			coapQakPhysicalThermometer.sendDispatch(testName, "input_temperature", "31.0")
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(temperatature_low, "Temperature high")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalThermometer.readResource()
					//println(resource)
					temperatature_low = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("low", true)
				} while(!temperatature_low)
				
				watchdog.cancel()
			}

		}
	}
	
	@Test fun TestParkServiceStatusGuiSetFanManualMode() {
		val testName = "TestFanManualMode"
		
		coapQakParkServiceStatusGUI.syncRequest(testName, "req_manager_fan_manual_mode", "X")
		
		// check manual mode on ParkServiceStatusGUI
		runBlocking {
			var manual_mode = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(manual_mode, "Fan automatic mode")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakParkServiceStatusGUI.readResource()
					//println(resource)
					manual_mode = resource.split("-").get(4).substringAfter("(").substringBefore(")").substringBefore(",").equals("manual", true)
				} while(!manual_mode)
				
				watchdog.cancel()
			}

		}
		
		// check manual mode on LogicalFan
		runBlocking {
			var manual_mode = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(manual_mode, "Fan automatic mode")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalFan.readResource()
					//println(resource)
					manual_mode = resource.split("-").get(1).substringAfter("(").substringBefore(")").equals("manual", true)
				} while(!manual_mode)
				
				watchdog.cancel()
			}

		}
		
		coapQakParkServiceStatusGUI.syncRequest(testName, "req_manager_fan_on", "X")
		
		// check fan on on ParkServiceStatusGUI
		runBlocking {
			var fan_on = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_on, "Fan off")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakParkServiceStatusGUI.readResource()
					//println(resource)
					fan_on = resource.split("-").get(4).substringAfter("(").substringBefore(")").substringAfter(",").equals("on", true)
				} while(!fan_on)
				
				watchdog.cancel()
			}

		}
		
		// check fan on on LogicalFan
		runBlocking {
			var fan_on = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_on, "Fan off")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalFan.readResource()
					//println(resource)
					fan_on = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("on", true)
				} while(!fan_on)
				
				watchdog.cancel()
			}

		}
		
		coapQakParkServiceStatusGUI.syncRequest(testName, "req_manager_fan_off", "X")
		
		// check fan off on ParkServiceStatusGUI
		runBlocking {
			var fan_off = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_off, "Fan on")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakParkServiceStatusGUI.readResource()
					//println(resource)
					fan_off = resource.split("-").get(4).substringAfter("(").substringBefore(")").substringAfter(",").equals("off", true)
				} while(!fan_off)
				
				watchdog.cancel()
			}

		}
		
		// check fan off on LogicalFan
		runBlocking {
			var fan_off = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_off, "Fan on")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalFan.readResource()
					//println(resource)
					fan_off = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("off", true)
				} while(!fan_off)
				
				watchdog.cancel()
			}

		}
		
	}
	
	
	@Test fun TestParkServiceStatusGuiSetFanAutomaticMode() {
		val testName = "TestFanManualMode"
		
		coapQakParkServiceStatusGUI.syncRequest(testName, "req_manager_fan_automatic_mode", "X")
		
		// check automatic mode on ParkServiceStatusGUI
		runBlocking {
			var automatic_mode = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(automatic_mode, "Fan manual mode")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakParkServiceStatusGUI.readResource()
					//println(resource)
					automatic_mode = resource.split("-").get(4).substringAfter("(").substringBefore(")").substringBefore(",").equals("automatic", true)
				} while(!automatic_mode)
				
				watchdog.cancel()
			}

		}
		
		// check automatic mode on LogicalFan
		runBlocking {
			var automatic_mode = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(automatic_mode, "Fan manual mode")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalFan.readResource()
					//println(resource)
					automatic_mode = resource.split("-").get(1).substringAfter("(").substringBefore(")").equals("automatic", true)
				} while(!automatic_mode)
				
				watchdog.cancel()
			}

		}
		
		coapQakPhysicalThermometer.sendDispatch(testName, "input_temperature", "38.0")
		
		// check fan on on ParkServiceStatusGUI
		runBlocking {
			var fan_on = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_on, "Fan off")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakParkServiceStatusGUI.readResource()
					//println(resource)
					fan_on = resource.split("-").get(4).substringAfter("(").substringBefore(")").substringAfter(",").equals("on", true)
				} while(!fan_on)
				
				watchdog.cancel()
			}

		}
		
		// check fan on on LogicalFan
		runBlocking {
			var fan_on = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_on, "Fan off")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalFan.readResource()
					//println(resource)
					fan_on = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("on", true)
				} while(!fan_on)
				
				watchdog.cancel()
			}

		}
		
		coapQakPhysicalThermometer.sendDispatch(testName, "input_temperature", "32.0")
		
		// check fan off on ParkServiceStatusGUI
		runBlocking {
			var fan_off = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_off, "Fan on")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakParkServiceStatusGUI.readResource()
					//println(resource)
					fan_off = resource.split("-").get(4).substringAfter("(").substringBefore(")").substringAfter(",").equals("off", true)
				} while(!fan_off)
				
				watchdog.cancel()
			}

		}
		
		// check fan off on LogicalFan
		runBlocking {
			var fan_off = false
			
			val watchdog = async {
				delay(35000)
				// after 35 sec check at_home
				assertTrue(fan_off, "Fan on")
			}
			async {
				do {
					delay(1000)
					val resource = coapQakLogicalFan.readResource()
					//println(resource)
					fan_off = resource.split("-").get(0).substringAfter("(").substringBefore(")").equals("off", true)
				} while(!fan_off)
				
				watchdog.cancel()
			}

		}
		
	}
	
}

