package it.unibo.automatedcarparking.mockdatagui

import it.unibo.automatedcarparking.mockdatagui.qakutil.QakTcpClient

object Thermometer {

    private lateinit var client : QakTcpClient

    fun init(thermometerConfig: ThermometerConfig) {
        client = QakTcpClient(thermometerConfig.hostname,
            thermometerConfig.port,
            "ctxthermometer",
            "thermometer")
        client.connect()
    }


    fun setTemperature(temperature : Int) {
        client.sendDispatch("mockdatagui", "input_temperature", "$temperature")
    }
}