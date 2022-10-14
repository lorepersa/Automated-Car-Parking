package it.unibo.automatedcarparking.mockdatagui

import it.unibo.automatedcarparking.mockdatagui.qakutil.QakTcpClient

object ParkingSlotsController {
    private lateinit var client : QakTcpClient

    fun init(businessLogicConfig: BusinessLogicConfig) {
        client = QakTcpClient(businessLogicConfig.hostname,
            businessLogicConfig.port,
            "ctxbusinesslogic",
            "parkingslotscontroller")
        client.connect()
    }


    fun reset() {
        client.sendDispatch("mockdatagui", "reset", "X")
    }
}