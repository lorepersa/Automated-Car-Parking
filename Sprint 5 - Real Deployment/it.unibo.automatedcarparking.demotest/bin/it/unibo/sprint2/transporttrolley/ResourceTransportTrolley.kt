package it.unibo.sprint1.transporttrolley

import it.unibo.sprint1.qakutils.IResource
import it.unibo.sprint1.test.transporttrolley.TransportTrolleyStatus
import it.unibo.sprint1.test.transporttrolley.Position
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import it.unibo.sprint1.qakutils.QakTcpClient
import it.unibo.sprint1.qakutils.CoapQAKObserver

class ResourceTransportTrolley(val hostname: String, val port: Int, val senderName: String) : IResource {

	var status = TransportTrolleyStatus(false, true, false, Position(0, 0, "downDir"))
	val mutex = Mutex()
	val gson = Gson()
	val tcpClient = QakTcpClient(hostname, port, "ctxbusinesslogic", "transporttrolley")
	val coapObserver = CoapQAKObserver(hostname, port, "ctxbusinesslogic", "transporttrolley", this)
	
	init {
		tcpClient.connect()
	}

	override fun notify(text: String) {
		runBlocking {
			mutex.withLock {
				status = gson.fromJson(text, TransportTrolleyStatus::class.java)
			}
		}
	}
	
	suspend fun isStopped() : Boolean {
		mutex.withLock {
			return status.stopped
		}
	}
	
	suspend fun isIdle() : Boolean {
		mutex.withLock {
			return !status.stopped && status.idle
		}
	}
	
	suspend fun isWorking() : Boolean {
		mutex.withLock {
			return !status.stopped && !status.idle
		}
	}

	fun start() {
		tcpClient.sendDispatch(senderName, "transport_trolley_start", "X")
	}

	fun stop() {
		tcpClient.sendDispatch(senderName, "transport_trolley_stop", "X")
	}

	suspend fun openSession() {
		tcpClient.syncRequest(senderName, "transport_trolley_new_job", "X")
	}

	suspend fun goTo(destination: String): Boolean {
		val response = tcpClient.syncRequest(senderName, "transport_trolley_go_to", destination)
		if (response.getName().equals("transport_trolley_error")) {
			println("ERROR GO TO REQUEST: " + response.getPayload())
			return false
		}
		return true
	}

	suspend fun takeOverCar(): Boolean {
		val response = tcpClient.syncRequest(senderName, "transport_trolley_take_over_car", "X")
		if (response.getName().equals("transport_trolley_error")) {
			println("ERROR TAKE OVER CAR REQUEST: " + response.getPayload())
			return false
		}
		return true
	}

	suspend fun releaseCar(): Boolean {
		val response = tcpClient.syncRequest(senderName, "transport_trolley_release_car", "X")
		if (response.getName().equals("transport_trolley_error")) {
			println("ERROR RELEASE CAR REQUEST: " + response.getPayload())
			return false
		}
		return true
	}

	suspend fun closeSession() {
		tcpClient.sendDispatch(senderName, "transport_trolley_job_done", "X")
	}
	
	private suspend fun doCarJob(takeOverCarPosition : String, releaseCarPosition : String) : Boolean {
		if (!goTo(takeOverCarPosition)) return false
		if (!takeOverCar())             return false
		if (!goTo(releaseCarPosition))  return false
		if (!releaseCar())              return false
		
		return true
	}

	suspend fun doCarPark(slotnum: Int) : Boolean {
		openSession()
		val jobOk = doCarJob("INDOOR", "$slotnum")
		closeSession()
		
		return jobOk
	}

	suspend fun doCarPickUp(slotnum: Int) : Boolean {
		openSession()
		val jobOk = doCarJob("$slotnum", "OUTDOOR")
		closeSession()
		
		return jobOk
	}
	
	suspend fun doSleepMode() {
		openSession()
		goTo("HOME")
		stop()
		closeSession()
	}
	
	suspend fun putInIdleState() {
		start()
		openSession()
		goTo("HOME")
		closeSession()
	}

}