package it.unibo.automatedcarparking.mockdatagui

import it.unibo.automatedcarparking.mockdatagui.qakutil.QakTcpClient

object OutSonar {

    private lateinit var client : QakTcpClient
    private var virtual = false

    fun init(outSonarConfig: OutSonarConfig) {
        client = QakTcpClient(outSonarConfig.hostname,
            outSonarConfig.port,
            "ctxoutsonar",
            "outsonar")
        client.connect()
        val config = client.syncRequest("mockdatagui", "outsonar_config", "X")
        if (config.getName() == "outsonar_virtual") {
            virtual = true
        } else {
            virtual = false
        }
    }


    fun setDistance(distance : Int) {
        client.sendDispatch("mockdatagui", "input_distance", "$distance")
    }
    
    fun isVirtual() : Boolean {
        return virtual
    }
}
