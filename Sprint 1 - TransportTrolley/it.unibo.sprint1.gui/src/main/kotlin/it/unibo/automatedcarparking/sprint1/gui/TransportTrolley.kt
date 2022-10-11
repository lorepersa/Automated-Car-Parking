package it.unibo.automatedcarparking.sprint1.gui

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import it.unibo.automatedcarparking.sprint1.gui.qakutil.CoapQAKObserver
import it.unibo.automatedcarparking.sprint1.gui.qakutil.IResource
import it.unibo.automatedcarparking.sprint1.gui.status.TransportTrolleyStatus
import itunibo.automatedcarparking.dsl.transporttrolley.transportTrolleyConnect
import itunibo.automatedcarparking.dsl.transporttrolley.transporttrolley

object TransportTrolley : IResource {

    private lateinit var qakObserver: CoapQAKObserver
    private val observers = mutableSetOf<ITransportTrolleyObserver>()
    private lateinit var latestStatus : TransportTrolleyStatus
    private val jsonMapper = ObjectMapper()

    fun init(businessLogicConfig : BusinessLogicConfig) {
        jsonMapper.registerModule(kotlinModule())

        qakObserver = CoapQAKObserver(businessLogicConfig.hostname,
            businessLogicConfig.port,
            "ctxbusinesslogic",
            "transporttrolley",
            this)

        transportTrolleyConnect(businessLogicConfig.hostname, businessLogicConfig.port)
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
        transporttrolley {
            start after 0
        }
    }

    suspend fun transportTrolleyStop() {
        transporttrolley {
            stop after 0
        }
    }

    suspend fun doCarPark(slotnum : Int) {
        transporttrolley {
            task {
                takeOverCar at "INDOOR"
                releaseCar at "$slotnum"
            }
        }
    }

    suspend fun doCarPickUp(slotnum : Int) {
        transporttrolley {
            task {
                takeOverCar at "$slotnum"
                releaseCar at "OUTDOOR"
            }
        }
    }
    
    suspend fun sleep() {
        transporttrolley {
            task {
                go to "HOME"
                stop after 0
            }
        }
    }

}