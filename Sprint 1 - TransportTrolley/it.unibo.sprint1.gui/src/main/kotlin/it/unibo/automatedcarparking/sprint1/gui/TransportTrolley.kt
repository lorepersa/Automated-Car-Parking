package it.unibo.automatedcarparking.sprint1.gui

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import it.unibo.automatedcarparking.sprint1.gui.qakutil.CoapQAKObserver
import it.unibo.automatedcarparking.sprint1.gui.qakutil.IResource
import it.unibo.automatedcarparking.sprint1.gui.qakutil.QakTcpClient
import it.unibo.automatedcarparking.sprint1.gui.status.TransportTrolleyStatus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object TransportTrolley : IResource {

    private val jobMutex = Mutex()
    private lateinit var jobClient : QakTcpClient

    private lateinit var ssClient : QakTcpClient
    private val ssMutex = Mutex()

    private lateinit var qakObserver: CoapQAKObserver
    private val observers = mutableSetOf<ITransportTrolleyObserver>()
    private lateinit var latestStatus : TransportTrolleyStatus
    private val jsonMapper = ObjectMapper()

    fun init(businessLogicConfig : BusinessLogicConfig) {
        jobClient = QakTcpClient(businessLogicConfig.hostname,
            businessLogicConfig.port,
            "ctxbusinesslogic",
            "transporttrolley")
        jobClient.connect()
        ssClient = QakTcpClient(businessLogicConfig.hostname,
            businessLogicConfig.port,
            "ctxbusinesslogic",
            "transporttrolley")
        ssClient.connect()

        jsonMapper.registerModule(kotlinModule())

        qakObserver = CoapQAKObserver(businessLogicConfig.hostname,
            businessLogicConfig.port,
            "ctxbusinesslogic",
            "transporttrolley",
            this)
    }

    @Synchronized
    fun getLatestStatusJson() : String {
        if (this::latestStatus.isInitialized) {
            return jsonMapper.writeValueAsString(latestStatus)
        }
        return ""
    }

    @Synchronized
    override fun notify(text: String) {
        println("status : $text")
        latestStatus = jsonMapper.readValue(text, TransportTrolleyStatus::class.java)
        observers.forEach{
            it.update(latestStatus)
        }
    }

    @Synchronized
    fun registerObserver(observer : ITransportTrolleyObserver) {
        observers.add(observer)
        if (this::latestStatus.isInitialized) {
            observer.update(latestStatus)
        }
    }

    @Synchronized
    fun deregisterObserver(observer : ITransportTrolleyObserver) {
        observers.remove(observer)
    }

    suspend fun transportTrolleyStart() {
        ssMutex.withLock {
            ssClient.sendDispatch("web", "transport_trolley_start", "X")
            println("sent start to transport trolley")
        }
    }

    suspend fun transportTrolleyStop() {
        ssMutex.withLock {
            ssClient.sendDispatch("web", "transport_trolley_stop", "X")
            println("sent stop to transport trolley")
        }
    }

    private suspend fun transportTrolleyOpenSession() {
        println("send new job to transport trolley")
        jobClient.syncRequest("web", "transport_trolley_new_job", "X")
        println("job accepted...")
    }

    private suspend fun transportTrolleyGoTo(destination : String) : Boolean {
        println("transport trolley go to $destination")
        val response = jobClient.syncRequest("web", "transport_trolley_go_to", destination)
        if (response.getName().equals("transport_trolley_error")) {
            println("ERROR GO TO REQUEST: " + response.getPayload())
            return false
        }
        println("transport trolley arrived at $destination")
        return true
    }

    private suspend fun transportTrolleyTakeOverCar() : Boolean {
        println("transport trolley take over car")
        val response = jobClient.syncRequest("web", "transport_trolley_take_over_car", "X")
        if (response.getName().equals("transport_trolley_error")) {
            println("ERROR TAKE OVER CAR REQUEST: " + response.getPayload())
            return false
        }
        println("transport trolley car taken over")
        return true
    }

    private suspend fun transportTrolleyReleaseCar() : Boolean {
        println("transport trolley release car")
        val response = jobClient.syncRequest("web", "transport_trolley_release_car", "X")
        if (response.getName().equals("transport_trolley_error")) {
            println("ERROR RELEASE CAR REQUEST: " + response.getPayload())
            return false
        }
        println("transport trolley car released")
        return true
    }

    private suspend fun transportTrolleyCloseSession() {
        jobClient.sendDispatch("web", "transport_trolley_job_done", "X")
        println("transport trolley job done")
    }

    suspend fun doCarPark(slotnum : Int) {
        jobMutex.withLock {
            transportTrolleyOpenSession()
            var ok = transportTrolleyGoTo("INDOOR")
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            ok = transportTrolleyTakeOverCar()
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            ok = transportTrolleyGoTo("$slotnum")
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            ok = transportTrolleyReleaseCar()
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            transportTrolleyCloseSession()
        }
    }

    suspend fun doCarPickUp(slotnum : Int) {
        jobMutex.withLock {
            transportTrolleyOpenSession()
            var ok = transportTrolleyGoTo("$slotnum")
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            ok = transportTrolleyTakeOverCar()
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            ok = transportTrolleyGoTo("OUTDOOR")
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            ok = transportTrolleyReleaseCar()
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            transportTrolleyCloseSession()
        }
    }
    
    suspend fun sleep() {
        jobMutex.withLock {
            transportTrolleyOpenSession()
            val ok = transportTrolleyGoTo("HOME")
            if (!ok) {
                transportTrolleyCloseSession()
                return
            }
            transportTrolleyStop()
            transportTrolleyCloseSession()
        }
    }

}