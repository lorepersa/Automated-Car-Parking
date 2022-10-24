package it.unibo.automatedcarparking.mockdatagui

import it.unibo.automatedcarparking.mockdatagui.qakutil.QakTcpClient

object WeightSensor {

    private lateinit var client : QakTcpClient

    fun init(weightSensorConfig: WeightSensorConfig) {
        client = QakTcpClient(weightSensorConfig.hostname,
            weightSensorConfig.port,
            "ctxweightsensor",
            "weightsensor")
        client.connect()
    }


    fun setWeight(weight : Int) {
        client.sendDispatch("mockdatagui", "input_weight", "$weight")
    }
}