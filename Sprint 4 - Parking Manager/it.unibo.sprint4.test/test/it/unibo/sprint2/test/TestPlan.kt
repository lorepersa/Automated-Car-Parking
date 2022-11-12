package it.unibo.sprint2.test

import org.junit.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlin.test.assertTrue
import it.unibo.automatedcarparking.test.resource.MockDataGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceStatusGUI
import kotlin.test.assertFalse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TestPlan() {


	suspend fun waitForIdleTransportTrolley() {
		while (true) {
			val status = ParkServiceStatusGUI.transportTrolleyGetStatus()

			if (!status.transportTrolleyStopped && status.transportTrolleyIdle) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForEngagedIndoorArea() {
		while (true) {
			val status = ParkServiceStatusGUI.indoorAreaGetStatus()

			if (status.indoorAreaEngaged) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForFreeIndoorArea() {
		while (true) {
			val status = ParkServiceStatusGUI.indoorAreaGetStatus()

			if (!status.indoorAreaReserved && !status.indoorAreaEngaged) {
				break
			}

			delay(1000)
		}
	}

	suspend fun waitForReservedIndoorArea() {
		while (true) {
			val status = ParkServiceStatusGUI.indoorAreaGetStatus()

			if (status.indoorAreaReserved) {
				break
			}

			delay(1000)
		}
	}

	@Test
	fun TestSuccessAcceptIn() {
		runBlocking {
			val testName = "TestSuccessAcceptIn"
			println("TEST: $testName")
			// preconditions
			MockDataGUI.resetParkingSlots()
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()
			waitForIdleTransportTrolley()
			delay(5000)
			// test
			val parkServiceGUI = ParkServiceGUI()
			val slotnum = parkServiceGUI.parkingCarInterest()
			assertTrue(slotnum > 0)
			delay(1000)
			// test passed, let the car enter
			MockDataGUI.setWeight(900)
			waitForEngagedIndoorArea()
			parkServiceGUI.carEnter(slotnum)
			delay(1000)
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()

			waitForIdleTransportTrolley()
			MockDataGUI.resetParkingSlots()
		}

	}

	@Test
	fun TestFullParkingArea() {
		runBlocking {
			val testName = "TestFullParkingArea"
			println("TEST: $testName")
			// preconditions
			MockDataGUI.resetParkingSlots()
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()
			waitForIdleTransportTrolley()
			delay(5000)
			val parkServiceGUI = ParkServiceGUI()
			while (true) {
				val slotnum = parkServiceGUI.parkingCarInterest()

				if (slotnum == 0) {
					break
				}
				
				MockDataGUI.setWeight(900)
				waitForEngagedIndoorArea()
				parkServiceGUI.carEnter(slotnum)
				MockDataGUI.setWeight(0)
				waitForFreeIndoorArea()
			}

			// if the test ends the parking area is full...
			waitForIdleTransportTrolley()
			MockDataGUI.resetParkingSlots()
		}
	}

	@Test
	fun TestOccupiedIndoorArea() {
		runBlocking {
			val testName = "TestOccupiedIndoorArea"
			println("TEST: $testName")
			// preconditions
			MockDataGUI.resetParkingSlots()
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()
			waitForIdleTransportTrolley()
			delay(5000)
			val firstParkServiceGUI = ParkServiceGUI()
			var slotnum = firstParkServiceGUI.parkingCarInterest()
			delay(1000)
			MockDataGUI.setWeight(900)
			waitForEngagedIndoorArea()
			val secondParkServiceGUI = ParkServiceGUI()

			
			coroutineScope {
				val secondRequest = async {
					secondParkServiceGUI.parkingCarInterest()
				}
				async {
					delay(15000) // wait 15 seconds
					assertTrue(secondRequest.isActive)
			
					// ok test passed, now handle the requests
			
					firstParkServiceGUI.carEnter(slotnum)
					delay(1000)
					MockDataGUI.setWeight(0)
					
					slotnum = secondRequest.await()
					MockDataGUI.setWeight(900)
					waitForEngagedIndoorArea()
					secondParkServiceGUI.carEnter(slotnum)
					MockDataGUI.setWeight(0)
					waitForFreeIndoorArea()
				}
			}

			waitForIdleTransportTrolley()
			MockDataGUI.resetParkingSlots()
		}
	}

	@Test
	fun TestWeightSensorDetectCar() {
		runBlocking {
			val testName = "TestWeightSensorDetectCar"
			println("TEST: $testName")
			// preconditions
			MockDataGUI.resetParkingSlots()
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()
			waitForIdleTransportTrolley()
			delay(5000)
			var indoorControllerStatus = ParkServiceStatusGUI.indoorAreaGetStatus()

			assertFalse(indoorControllerStatus.indoorAreaReserved)
			assertFalse(indoorControllerStatus.indoorAreaEngaged)

			val parkServiceGUI = ParkServiceGUI()
			val slotnum = parkServiceGUI.parkingCarInterest()
			delay(1000)
			MockDataGUI.setWeight(900)
			waitForEngagedIndoorArea()
			indoorControllerStatus = ParkServiceStatusGUI.indoorAreaGetStatus()
			assertTrue(indoorControllerStatus.indoorAreaEngaged)

			// test passed here
			// leave the indoor area
			parkServiceGUI.carEnter(slotnum)
			delay(1000)
			MockDataGUI.setWeight(0)
			waitForFreeIndoorArea()
			waitForIdleTransportTrolley()
			MockDataGUI.resetParkingSlots()
		}
	}
}