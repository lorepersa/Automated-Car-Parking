package it.unibo.automatedcarparking.test.resource.qactor

import it.unibo.automatedcarparking.test.qakutils.IResource
import it.unibo.automatedcarparking.test.qakutils.CoapQAKObserver
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock

data class IndoorControllerStatus(var reserved : Boolean,
								  var engagedByCar : Boolean,
								  var carEnterTimeoutAlarm : Boolean)

object IndoorController : IResource {
	private val address = "localhost"
	private val port = 8065
	private val context = "ctxbusinesslogic"
	private val name = "indoorcontroller"
	
	private val observer = CoapQAKObserver(address, port, context, name, this)
	private var status = IndoorControllerStatus(false, false, false)
	
	private val gson = Gson()
	private val mutex = Mutex()
	
	override fun notify(text: String) {
		runBlocking {
			mutex.withLock {
				status = gson.fromJson(text, IndoorControllerStatus::class.java)
			}
		}
	}
	
	fun getStatus() : IndoorControllerStatus {
		lateinit var s : IndoorControllerStatus
		runBlocking {
			mutex.withLock {
				s = status.copy()
			}
		}
		
		return s
	}
}