package it.unibo.sprint4.test

import de.regnis.q.sequence.core.QSequenceAssert.assertTrue
import it.unibo.automatedcarparking.test.resource.MockDataGUI
import it.unibo.automatedcarparking.test.resource.ParkServiceStatusGUI
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


    @Before
    fun cleanup() {
        runBlocking {
            ParkServiceStatusGUI.transportTrolleyStart()
            waitForIdleTransportTrolley()
            MockDataGUI.setWeight(0)
            MockDataGUI.setDistance(900)
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

    @Test
    fun TestThermometerTemperatureHigh() {
        runBlocking {
            val testName = "TestThermometerTemperatureHigh"
            println("RUN TEST: $testName")

            MockDataGUI.setTemperature(38)

            var temperature_high = false

            coroutineScope {

                val watchdog = async {
                    delay(35000)
                    assertTrue(temperature_high, "Temperature low")
                }
                val controller = async {
                    do {
                        delay(1000)
                        temperature_high = ParkServiceStatusGUI.temperatureGetStatus().parkingAreaTemperatureHigh
                    } while (!temperature_high)

                    watchdog.cancel()
                }
            }

            MockDataGUI.setTemperature(25)
        }
    }

    @Test
    fun TestThermometerTemperatureLow() {
        runBlocking {
            val testName = "TestThermometerTemperatureLow"
            println("RUN TEST: $testName")

            MockDataGUI.setTemperature(40)
            waitForTemperatureHigh()
            var temperatature_low = false

            MockDataGUI.setTemperature(25)

            coroutineScope {

                val watchdog = async {
                    delay(35000)
                    assertTrue(temperatature_low, "Temperature high")
                }
                val controller = async {
                    do {
                        delay(1000)
                        temperatature_low = !ParkServiceStatusGUI.temperatureGetStatus().parkingAreaTemperatureHigh
                    } while (!temperatature_low)

                    watchdog.cancel()
                }
            }

        }
    }

    @Test
    fun TestParkServiceStatusGuiSetFanManualMode() {
        runBlocking {
            val testName = "TestFanManualMode"
            println("RUN TEST: $testName")

            ParkServiceStatusGUI.fanAutomaticMode()
            waitForFanAutomatic()

            ParkServiceStatusGUI.fanManualMode()
            waitForFanManual()
            ParkServiceStatusGUI.fanOn()
            waitForFanOn()
            ParkServiceStatusGUI.fanOff()
            waitForFanOff()
        }
    }


    @Test
    fun TestParkServiceStatusGuiSetFanAutomaticMode() {
        runBlocking {
            val testName = "TestFanManualMode"
            println("RUN TEST: $testName")

            ParkServiceStatusGUI.fanManualMode()
            waitForFanManual()

            ParkServiceStatusGUI.fanAutomaticMode()
            waitForFanAutomatic()
            waitForFanOff()
            MockDataGUI.setTemperature(40)
            waitForTemperatureHigh()
            waitForFanOn()
            MockDataGUI.setTemperature(25)
            waitForTemperatureLow()
            waitForFanOff()
        }
    }

    @Test
    fun TestParkServiceStatusGuiResumeTransportTrolley() {
        runBlocking {
            val testName = "TestParkServiceStatusGuiResumeTransportTrolley"
            println("RUN TEST: $testName")

            ParkServiceStatusGUI.transportTrolleyStop()
            waitForStoppedTransportTrolley()
            ParkServiceStatusGUI.transportTrolleyStart()
            waitForNotStoppedTransportTrolley()
        }
    }

}