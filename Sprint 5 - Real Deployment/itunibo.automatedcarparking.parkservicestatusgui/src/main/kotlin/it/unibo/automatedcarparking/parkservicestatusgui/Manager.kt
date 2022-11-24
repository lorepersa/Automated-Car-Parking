package it.unibo.automatedcarparking.parkservicestatusgui

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import it.unibo.automatedcarparking.parkservicestatusgui.qakutil.CoapQAKObserver
import it.unibo.automatedcarparking.parkservicestatusgui.qakutil.IResource
import it.unibo.automatedcarparking.parkservicestatusgui.qakutil.RemoteQActor
import it.unibo.automatedcarparking.parkservicestatusgui.qakutil.RemoteQakContext
import it.unibo.automatedcarparking.parkservicestatusgui.status.AutomatedCarParkingStatus

object Manager : IResource {

    private lateinit var qakObserver: CoapQAKObserver
    private lateinit var managerControllerActor : RemoteQActor
    private val jsonMapper = ObjectMapper()
    private val observers = mutableSetOf<IAutomatedCarParkingStatusObserver>()
    private lateinit var latestStatus : AutomatedCarParkingStatus

    fun init(context : RemoteQakContext) {
        jsonMapper.registerModule(kotlinModule())

        managerControllerActor = RemoteQActor("managercontroller", context)

        qakObserver = CoapQAKObserver(context.hostname,
            context.port,
            "ctxbusinesslogic",
            "statusaggregator",
            this)
    }

    @Synchronized
    override fun notify(text: String) {
        latestStatus = jsonMapper.readValue(text, AutomatedCarParkingStatus::class.java)
        observers.forEach{
            it.update(latestStatus)
        }
    }

    @Synchronized
    fun registerObserver(observer : IAutomatedCarParkingStatusObserver) {
        observers.add(observer)
        if (this::latestStatus.isInitialized) {
            observer.update(latestStatus)
        }
    }

    @Synchronized
    fun deregisterObserver(observer : IAutomatedCarParkingStatusObserver) {
        observers.remove(observer)
    }

    @Synchronized
    fun getLatestStatusJson() : String {
        if (this::latestStatus.isInitialized) {
            return jsonMapper.writeValueAsString(latestStatus)
        }
        return ""
    }

    fun fanOn() {
        managerControllerActor.dispatch("manager_fan_on", "X")
    }

    fun fanOff() {
        managerControllerActor.dispatch("manager_fan_off", "X")
    }

    fun fanManualMode() {
        managerControllerActor.dispatch("manager_fan_manual_mode", "X")
    }

    fun fanAutomaticMode() {
        managerControllerActor.dispatch("manager_fan_automatic_mode", "X")
    }

    fun transportTrolleyStart() {
        managerControllerActor.dispatch("manager_transport_trolley_start", "X")
    }

    fun transportTrolleyStop() {
        managerControllerActor.dispatch("manager_transport_trolley_stop", "X")
    }

    fun weightSensorInfoOn() {
        managerControllerActor.dispatch("weightsensor_info_on", "X")
    }

    fun weightSensorInfoOff() {
        managerControllerActor.dispatch("weightsensor_info_off", "X")
    }

    fun outSonarInfoOn() {
        managerControllerActor.dispatch("outsonar_info_on", "X")
    }

    fun outSonarInfoOff() {
        managerControllerActor.dispatch("outsonar_info_off", "X")
    }


}