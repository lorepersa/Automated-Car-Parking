package it.unibo.sprint1.test

import org.junit.AfterClass
import kotlin.test.assertEquals
import org.junit.Test
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import org.junit.After
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.gson.JsonParser
import it.unibo.sprint1.qakutils.CoapQAKObserver
import it.unibo.sprint1.transporttrolley.ResourceTransportTrolley
import kotlin.test.assertTrue
import it.unibo.sprint1.test.transporttrolley.Position
import org.junit.runners.Parameterized
import org.junit.runner.RunWith

// TODO if parameters are needed...
//@RunWith(Parameterized::class)
class TestPlan() {
	val slotnum = 5
	val transportTrolley = ResourceTransportTrolley("127.0.0.1", 8065, "testplan")
	val homePosition = Position(0,0,"downDir")
	
	// TODO if parameters are needed...
	companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() : Collection<Array<Any>> {
            return listOf(
                    arrayOf(1),
                    arrayOf(2),
                    arrayOf(3),
                    arrayOf(4),
                    arrayOf(5),
					arrayOf(6),
            )
        }
    }
	
	@Test
	fun TestCarPark() {
		val testName = "TestCarPark"
		println("TEST: $testName")
		println("SLOTNUM: $slotnum")
		runBlocking {
			// initial conditions
			transportTrolley.start()
			
			// test code
			transportTrolley.openSession()
			assertTrue(transportTrolley.goTo("INDOOR"))
			assertTrue(transportTrolley.takeOverCar())
			assertTrue(transportTrolley.goTo("$slotnum"))
			assertTrue(transportTrolley.releaseCar())
			transportTrolley.closeSession()
		}
	}
	
	@Test
	fun TestCarPickUp() {
		val testName = "TestCarPickUp"
		println("TEST: $testName")
		println("SLOTNUM: $slotnum")
		runBlocking {
			// initial conditions
			transportTrolley.start()
			
			// test code
			transportTrolley.openSession()
			assertTrue(transportTrolley.goTo("$slotnum"))
			assertTrue(transportTrolley.takeOverCar())
			assertTrue(transportTrolley.goTo("OUTDOOR"))
			assertTrue(transportTrolley.releaseCar())
			transportTrolley.closeSession()
		}
	}
	
	@Test
	fun TestAutomaticBackToHomeAfterCarPark() {
		val testName = "TestAutomaticBackToHomeAfterCarPark"
		println("TEST: $testName")
		println("SLOTNUM: $slotnum")
		runBlocking {
			// initial conditions
			transportTrolley.start()
			
			// test code
			transportTrolley.openSession()
			assertTrue(transportTrolley.goTo("INDOOR"))
			assertTrue(transportTrolley.takeOverCar())
			assertTrue(transportTrolley.goTo("$slotnum"))
			assertTrue(transportTrolley.releaseCar())
			assertTrue(transportTrolley.isWorking()) // assert working
			transportTrolley.closeSession()
			
			while (true) {
				if (transportTrolley.isIdle()) {
					break
				}
				delay(1000)
				println("not idle...")
			}
		}
	}
	
	@Test
	fun TestAutomaticBackToHomeAfterCarPickUp() {
		val testName = "TestAutomaticBackToHomeAfterCarPickUp"
		println("TEST: $testName")
		println("SLOTNUM: $slotnum")
		runBlocking {
			// initial conditions
			transportTrolley.start()
			
			// test code
			transportTrolley.openSession()
			assertTrue(transportTrolley.goTo("$slotnum"))
			assertTrue(transportTrolley.takeOverCar())
			assertTrue(transportTrolley.goTo("OUTDOOR"))
			assertTrue(transportTrolley.releaseCar())
			assertTrue(transportTrolley.isWorking()) // assert working
			transportTrolley.closeSession()
			
			while (true) {
				if (transportTrolley.isIdle()) {
					break
				}
				delay(1000)
				println("not idle...")
			}
		}
	}

	
	@Test
	fun TestSleepMode() {
		val testName = "TestSleepMode"
		println("TEST: $testName")
		runBlocking {
			// initial conditions
			// not stopped
			transportTrolley.start()
			// not at home
			transportTrolley.openSession()
			assertTrue(transportTrolley.goTo("OUTDOOR"))
			transportTrolley.closeSession()

			
			// test code
			transportTrolley.openSession()
			assertTrue(transportTrolley.goTo("HOME"))
			transportTrolley.stop()
			transportTrolley.closeSession()
			
			while (true) {
				if (transportTrolley.isStopped()) {
					break
				}
				delay(1000)
				println("not stopped...")
			}
		}
	}
	
	@Test
	fun TestStartAndStop() {
		val testName = "TestStartAndStop"
		println("TEST: $testName")
		runBlocking {
			// initial conditions
			// not stopped
			transportTrolley.start()

			
			// test code
			transportTrolley.stop()
			
			while (true) {
				if (transportTrolley.isStopped()) {
					break
				}
				delay(1000)
				println("not stopped...")
			}
			
			transportTrolley.start()
			
			while (true) {
				if (!transportTrolley.isStopped()) {
					break
				}
				delay(1000)
				println("stopped...")
			}
		}
	}
}