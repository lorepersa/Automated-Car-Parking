package it.unibo.automatedcarparking.parkservicegui

import it.unibo.automatedcarparking.parkservicegui.qakutil.RemoteQActor
import it.unibo.automatedcarparking.parkservicegui.qakutil.RemoteQakContext

object IndoorArea {

    private lateinit var actor : RemoteQActor

    fun init(context : RemoteQakContext) {
        actor = RemoteQActor("indoorcontroller", context)
    }

    fun enterIndoorArea() : Int {
        val response = actor.request("parking_car_interest", "X")
        return try {
            response.getPayload().toInt()
        } catch (e : Exception) {
            0
        }

    }

    @Synchronized
    fun enterParkingArea(slotnum : Int) : String {
        val response = actor.request("car_enter", "$slotnum")
        return try {
            response.getPayload()
        } catch (e: Exception) {
            "WebServerError"
        }
    }
}