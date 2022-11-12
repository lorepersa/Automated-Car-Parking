package it.unibo.automatedcarparking.mockdatagui

import it.unibo.automatedcarparking.mockdatagui.qakutil.QakTcpClient

object OutSonar {

    private lateinit var client : QakTcpClient

    fun init(outSonarConfig: OutSonarConfig) {
        client = QakTcpClient(outSonarConfig.hostname,
            outSonarConfig.port,
            "ctxoutsonar",
            "outsonar")
        client.connect()
    }


    fun setDistance(distance : Int) {
        client.sendDispatch("mockdatagui", "input_distance", "$distance")
    }
}