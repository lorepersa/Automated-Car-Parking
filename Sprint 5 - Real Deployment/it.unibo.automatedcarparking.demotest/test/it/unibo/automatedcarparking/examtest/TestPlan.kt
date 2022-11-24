package it.unibo.automatedcarparking.examtest

import it.unibo.automatedcarparking.test.resource.MockDataGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceStatusGUI
import junit.framework.Assert.fail
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

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

    suspend fun waitForStoppedTransportTrolley() {
        while (true) {
            val status = ParkServiceStatusGUI.transportTrolleyGetStatus()

            if (status.transportTrolleyStopped) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForNotStoppedTransportTrolley() {
        while (true) {
            val status = ParkServiceStatusGUI.transportTrolleyGetStatus()

            if (!status.transportTrolleyStopped) {
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

    suspend fun waitForFreeOutdoorArea() {
        while (true) {
            val status = ParkServiceStatusGUI.outdoorAreaGetStatus()

            if (!status.outdoorAreaReserved && !status.outdoorAreaEngaged) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForTemperatureLow() {
        while (true) {
            val status = ParkServiceStatusGUI.temperatureGetStatus()

            if (!status.parkingAreaTemperatureHigh) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForTemperatureHigh() {
        while (true) {
            val status = ParkServiceStatusGUI.temperatureGetStatus()

            if (status.parkingAreaTemperatureHigh) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForFanOff() {
        while (true) {
            val status = ParkServiceStatusGUI.fanGetStatus()

            if (!status.fanOn) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForFanOn() {
        while (true) {
            val status = ParkServiceStatusGUI.fanGetStatus()

            if (status.fanOn) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForFanManual() {
        while (true) {
            val status = ParkServiceStatusGUI.fanGetStatus()

            if (!status.fanAutomatic) {
                break
            }

            delay(1000)
        }
    }

    suspend fun waitForFanAutomatic() {
        while (true) {
            val status = ParkServiceStatusGUI.fanGetStatus()

            if (status.fanAutomatic) {
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

    @Before
    fun cleanup() {
        runBlocking {
            ParkServiceStatusGUI.transportTrolleyStart()
            waitForIdleTransportTrolley()
            MockDataGUI.setWeight(0)
            waitForFreeIndoorArea()
            waitForFreeOutdoorArea()
            MockDataGUI.resetParkingSlots()
            MockDataGUI.setTemperature(25)
            waitForTemperatureLow()
            ParkServiceStatusGUI.fanManualMode()
            waitForFanManual()
            ParkServiceStatusGUI.fanOff()
            waitForFanOff()
            delay(5000)
        }
    }

    class StepPrinter() {
        private var stepNumber = 1
        private var subStepNumber = 1

        fun newStep() {
            stepNumber++
            subStepNumber = 1
        }
        fun println(message : String) {
            kotlin.io.println("$stepNumber.$subStepNumber $message ...")
            subStepNumber++
        }
    }

    @Test
    fun TestTemperatureHigh() {
        runBlocking {
            val testName = "TestTemperatureHigh"
            println("RUN TEST: $testName")

            val printer = StepPrinter()

            printer.println("Set Fan in Automatic Mode")
            ParkServiceStatusGUI.fanAutomaticMode()
            printer.newStep()
            val parkServiceGUI = ParkServiceGUI()
            printer.println("Client asks to enter into the Indoor Area")
            val slotnum = parkServiceGUI.parkingCarInterest()
            if (slotnum > 0) {
                printer.println( "Received Slotnum $slotnum")
                delay(2000)
                printer.println( "Client is entering the Indoor Area")
                MockDataGUI.setWeight(150)
                delay(100)
                MockDataGUI.setWeight(300)
                delay(100)
                MockDataGUI.setWeight(450)
                delay(100)
                MockDataGUI.setWeight(600)
                delay(100)
                MockDataGUI.setWeight(750)
                waitForEngagedIndoorArea()
            } else {
                fail("Full Parking Area")
            }
            delay(3000)
            printer.newStep()
            printer.println("Client presses Car Enter button")
            var tokenid = ""
            coroutineScope {
                async {
                    tokenid = parkServiceGUI.carEnter(slotnum)
                }
                async {
                    // wait until the transport trolley reach a certain position...
                    while (true) {
                        val status = ParkServiceStatusGUI.transportTrolleyGetStatus()
                        if (status.transportTrolleyCoordinate.column >= 2 && status.transportTrolleyCoordinate.row == 0) {
                            break
                        }
                        delay(100)
                    }
                    MockDataGUI.setTemperature(36)
                    waitForTemperatureHigh()
                    printer.println("Temperature High, need to stop the Transport Trolley")
                    ParkServiceStatusGUI.transportTrolleyStop()
                    delay(3000)
                    MockDataGUI.setTemperature(40)
                    delay(1000)
                    MockDataGUI.setTemperature(37)
                    MockDataGUI.setTemperature(32)
                    printer.println("Temperature High, now resume the Transport Trolley")
                    ParkServiceStatusGUI.transportTrolleyStart()
                    MockDataGUI.setTemperature(25)
                }
            }
            printer.println("Received TOKENID $tokenid")
            MockDataGUI.setWeight(0)
            waitForIdleTransportTrolley()
            printer.newStep()
            printer.println("Now let the car exit from the parking area")
            if (!parkServiceGUI.carPickUp(tokenid)) {
                fail("Car Pick Up refused")
            }
            printer.newStep()
            printer.println("Success")
        }
    }
}
