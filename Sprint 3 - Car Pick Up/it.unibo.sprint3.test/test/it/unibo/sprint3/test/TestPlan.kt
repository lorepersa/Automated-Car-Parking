package it.unibo.sprint3.test

import org.junit.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlin.test.assertTrue
import it.unibo.automatedcarparking.test.resource.MockDataGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceGUI
import it.unibo.automatedcarparking.test.resource.TransportTrolleyGUI
import it.unibo.automatedcarparking.test.resource.qactor.IndoorController
import it.unibo.automatedcarparking.test.resource.qactor.OutdoorController
import kotlin.test.assertFalse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.junit.Before

class TestPlan() {

	suspend fun waitForIdleTransportTrolley() {
		while (true) {
			val status = TransportTrolleyGUI.transportTrolleyGetStatus()

			if (!status.stopped && status.idle) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForEngagedIndoorArea() {
		while (true) {
			val status = IndoorController.getStatus()

			if (status.engagedByCar) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForFreeIndoorArea() {
		while (true) {
			val status = IndoorController.getStatus()

			if (!status.reserved && !status.engagedByCar) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForReservedIndoorArea() {
		while (true) {
			val status = IndoorController.getStatus()

			if (status.reserved) {
				break
			}

			delay(1000)
		}
	}
	
	suspend fun waitForEngagedOutdoorArea() {
		while (true) {
			val status = OutdoorController.getStatus()

			if (status.engagedByCar) {
				break
			}

			delay(1000)
		}
	}
	
	suspend fun waitForFreeOutdoorArea() {
		while (true) {
			val status = OutdoorController.getStatus()

			if (!status.reserved && !status.engagedByCar) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForDtfreeAlarm() {
		while (true) {
			val status = OutdoorController.getStatus()

			if (status.dtfreeTimeoutAlarm) {
				break
			}

			delay(1000)
		}
	}


	suspend fun waitForReservedOutdoorArea() {
		while (true) {
			val status = OutdoorController.getStatus()

			if (status.reserved) {
				break
			}

			delay(1000)
		}
	}

	suspend fun enterIndoorArea(parkServiceGUI: ParkServiceGUI) : String {
		val slotnum = parkServiceGUI.parkingCarInterest()
		if (slotnum > 0) {
			MockDataGUI.setWeight(900)
			waitForEngagedIndoorArea()
			val tokenid = parkServiceGUI.carEnter(slotnum)
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()

			return tokenid
		}

		return "FullParkingArea"
	}

	@Before
	fun cleanup() {
		runBlocking {
			waitForIdleTransportTrolley()
			MockDataGUI.setWeight(0)
			MockDataGUI.setDistance(900)
			waitForFreeIndoorArea()
			waitForFreeOutdoorArea()
			MockDataGUI.resetParkingSlots()
			delay(5000)
		}
	}

	@Test
	fun TestTripOneCar() {
		runBlocking {
			val testName = "TestTripOneCar"
			println("TEST: $testName")
			// test
			val parkServiceGUI = ParkServiceGUI()
			val tokenid = enterIndoorArea(parkServiceGUI)

			waitForIdleTransportTrolley()

			MockDataGUI.setDistance(0)
			val pick_up_done = parkServiceGUI.carPickUp(tokenid)
			assertTrue(pick_up_done)

			waitForEngagedOutdoorArea()

			// user takes the car
			MockDataGUI.setDistance(900)
			waitForFreeOutdoorArea()
		}
	}

	@Test
	fun TestInvalidTokenId() {
		runBlocking {
			val testName = "TestInvalidTokenId"
			println("TEST: $testName")
			// test
			val parkServiceGUI = ParkServiceGUI()
			val pick_up_done = parkServiceGUI.carPickUp("prova")
			assertFalse(pick_up_done)
		}
	}

	@Test
	fun TestOccupiedOutdoorArea() {
		runBlocking {
			val testName = "TestOccupiedOutdoorArea"
			println("TEST: $testName")
			// test
			val firstParkServiceGUI = ParkServiceGUI()
			val firstTokenId = enterIndoorArea(firstParkServiceGUI)
			val secondParkServiceGUI = ParkServiceGUI()
			val secondTokenId = enterIndoorArea(secondParkServiceGUI)
			MockDataGUI.setDistance(0)
			firstParkServiceGUI.carPickUp(firstTokenId)

			coroutineScope {
				val secondRequest = async {
					secondParkServiceGUI.carPickUp(secondTokenId)
					waitForEngagedOutdoorArea()

					// user takes the car
					MockDataGUI.setDistance(900)
					waitForFreeOutdoorArea()
				}
				async {
					waitForEngagedOutdoorArea()
					delay(15000) // wait 15 seconds
					assertTrue(secondRequest.isActive)

					// ok test passed, now handle the requests
					// user takes the car
					MockDataGUI.setDistance(900)

					// second car
					MockDataGUI.setDistance(0)
				}
			}
		}
	}


	@Test
	fun TestOutSonarDetectCar() {
		runBlocking {
			val testName = "TestOutSonarDetectCar"
			println("TEST: $testName")
			var outdoorControllerStatus = OutdoorController.getStatus()

			assertFalse(outdoorControllerStatus.reserved)
			assertFalse(outdoorControllerStatus.engagedByCar)

			val parkServiceGUI = ParkServiceGUI()
			val tokenid = enterIndoorArea(parkServiceGUI)
			waitForIdleTransportTrolley()
			MockDataGUI.setDistance(0)
			parkServiceGUI.carPickUp(tokenid)
			waitForEngagedOutdoorArea()
			assertTrue(OutdoorController.getStatus().engagedByCar)

			// test passed here
			// leave the outdoor area
			MockDataGUI.setDistance(900)
			waitForFreeOutdoorArea()
		}
	}

	@Test
	fun TestDtfreeAlarm() {
		runBlocking {
			val testName = "TestDtfreeAlarm"
			println("TEST: $testName")
			val parkServiceGUI = ParkServiceGUI()
			val tokenid = enterIndoorArea(parkServiceGUI)
			waitForIdleTransportTrolley()
			MockDataGUI.setDistance(0)
			parkServiceGUI.carPickUp(tokenid)
			waitForEngagedOutdoorArea()
			waitForDtfreeAlarm()
			assertTrue(OutdoorController.getStatus().engagedByCar)

			// test passed here
			// leave the outdoor area
			MockDataGUI.setDistance(900)
			waitForFreeOutdoorArea()
		}
	}

}