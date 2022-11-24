package it.unibo.automatedcarparking.demotest

import it.unibo.automatedcarparking.test.resource.MockDataGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceStatusGUI
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import junit.framework.Assert.fail

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
            //MockDataGUI.resetParkingSlots()
            MockDataGUI.setTemperature(25)
            waitForTemperatureLow()
            ParkServiceStatusGUI.fanManualMode()
            waitForFanManual()
            ParkServiceStatusGUI.fanOff()
            waitForFanOff()
            delay(5000)
        }
    }

    @Test
    fun TestAlTermineDelLavoro() {
        runBlocking {
            val testName = "TestAlTermineDelLavoro"
            println("RUN TEST: $testName")
            
            println("enterrequest - send ...")
            val parkServiceGUI1 = ParkServiceGUI()
            val slotnum = parkServiceGUI1.parkingCarInterest()
            if (slotnum > 0) {
                delay(2000)
                println("enterrequest - now enter in the indoor area ...")
            	MockDataGUI.setWeight(900)
                waitForEngagedIndoorArea()
                println("enterrequest - indoor area occupied ...")
            } else {
                fail("enterrequest - FAILURE ...")
            }
            println("wait 2 seconds ...")
            delay(2000) // wait 2 s per requirements
            coroutineScope {
                val carenterrequest = async {
                	println("carenter - send ...")
                    val tokenid = parkServiceGUI1.carEnter(slotnum)
                    MockDataGUI.setWeight(0)
                    waitForFreeIndoorArea()
                    println("carenter - done ...")
                }
                val pickuprequest = async {
                    println("pickup request - wait 2 seconds ...")
                    delay(2000)
                    println("pickup request - send ...")
                    val parkServiceGUI2 = ParkServiceGUI()
                    if (!parkServiceGUI2.carPickUp("abcd1")) {
                    	fail("pickup request - FAILURE ...")
                    }
                    println("pickup request - done ...")
                    waitForFreeOutdoorArea()
                }
            }
            
            waitForIdleTransportTrolley()
        }
    }
}
