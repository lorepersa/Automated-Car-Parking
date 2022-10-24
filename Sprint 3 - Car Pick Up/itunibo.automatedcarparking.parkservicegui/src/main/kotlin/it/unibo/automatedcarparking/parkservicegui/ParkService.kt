package it.unibo.automatedcarparking.parkservicegui

import it.unibo.automatedcarparking.parkservicegui.qakutil.RemoteQActor
import it.unibo.automatedcarparking.parkservicegui.qakutil.RemoteQakContext

object ParkingCarInterest {
    private lateinit var actor : RemoteQActor

    fun init(context : RemoteQakContext) {
        actor = RemoteQActor("parkservicecontroller", context)
    }

    @Synchronized
    fun enterIndoorArea() : Int {
        val response = actor.request("parking_car_interest", "X")
        try {
            return response.getPayload().toInt()
        } catch (e : Exception) {
            return 0
        }
    }
}

object CarEnterClient {
    private lateinit var actor : RemoteQActor

    fun init(context : RemoteQakContext) {
        actor = RemoteQActor("parkservicecontroller", context)
    }

    @Synchronized
    fun enterParkingArea(slotnum : Int) : String {
        val response = actor.request("car_enter", "$slotnum")

        return response.getPayload()
    }
}

object CarPickUpClient {
    private lateinit var actor : RemoteQActor

    fun init(context : RemoteQakContext) {
        actor = RemoteQActor("parkservicecontroller", context)
    }

    @Synchronized
    fun exitParkingArea(tokenid : String) : String {
        val response = actor.request("car_pick_up", tokenid)

        if (response.getName() == "accept_out_success") {
            return "ok"
        } else {
            return "failure"
        }
    }
}

object ParkService {

    fun init(context : RemoteQakContext) {
        ParkingCarInterest.init(context)
        CarEnterClient.init(context)
        CarPickUpClient.init(context)
    }

    fun enterIndoorArea() : Int {
        return ParkingCarInterest.enterIndoorArea()
    }

    fun enterParkingArea(slotnum : Int) : String {
        return CarEnterClient.enterParkingArea(slotnum)
    }

    fun exitParkingArea(tokenid : String) : String {
        return CarPickUpClient.exitParkingArea(tokenid)
    }
}
