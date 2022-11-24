package it.unibo.automatedcarparking.parkservicestatusgui.status

data class TemperatureStatus(var parkingAreaTemperature : Int = 0,
                             var parkingAreaTemperatureHigh : Boolean = false) {
    constructor(status : AutomatedCarParkingStatus) :
            this(parkingAreaTemperature=status.parkingAreaTemperature,
                parkingAreaTemperatureHigh=status.parkingAreaTemperatureHigh
            )
}