package it.unibo.automatedcarparking.parkservicestatusgui.qakutil

class RemoteQActor(val actorName : String, val context : RemoteQakContext) {

    fun request(msgId : String, payload : String) : Message {
        return context.syncRequest(msgId, payload, actorName)
    }

    fun dispatch(msgId : String, payload : String) {
        context.sendDispatch(msgId, payload, actorName)
    }
}