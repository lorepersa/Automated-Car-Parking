package it.unibo.automatedcarparking.test.resource.qactor

import com.google.gson.Gson
import it.unibo.automatedcarparking.test.qakutils.CoapQAKObserver
import it.unibo.automatedcarparking.test.qakutils.IResource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


data class OutdoorControllerStatus(var reserved : Boolean,
                                   var engagedByCar : Boolean,
                                   var dtfreeTimeoutAlarm : Boolean)

object OutdoorController : IResource {
    private val address = "localhost"
    private val port = 8065
    private val context = "ctxbusinesslogic"
    private val name = "outdoorcontroller"

    private val observer = CoapQAKObserver(address, port, context, name, this)
    private var status = OutdoorControllerStatus(false, false, false)

    private val gson = Gson()
    private val mutex = Mutex()

    override fun notify(text: String) {
        runBlocking {
            mutex.withLock {
                status = gson.fromJson(text, OutdoorControllerStatus::class.java)
            }
        }
    }

    fun getStatus() : OutdoorControllerStatus {
        lateinit var s : OutdoorControllerStatus
        runBlocking {
            mutex.withLock {
                s = status.copy()
            }
        }

        return s
    }
}