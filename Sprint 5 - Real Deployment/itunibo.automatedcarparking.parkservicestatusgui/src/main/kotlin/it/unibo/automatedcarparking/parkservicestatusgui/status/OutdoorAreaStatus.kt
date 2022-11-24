package it.unibo.automatedcarparking.parkservicestatusgui.status

data class OutdoorAreaStatus(var outsonarOn : Boolean = false,
                             var outdoorAreaDistance : Int = 0,
                             var outdoorAreaReserved : Boolean = false,
                             var outdoorAreaEngaged : Boolean = false,
                             var outdoorAreaDTFREETimeoutAlarm : Boolean = false) {
    constructor(status : AutomatedCarParkingStatus) :
            this(outsonarOn=status.outsonarOn,
                outdoorAreaDistance=status.outdoorAreaDistance,
                outdoorAreaReserved=status.outdoorAreaReserved,
                outdoorAreaEngaged=status.outdoorAreaEngaged,
                outdoorAreaDTFREETimeoutAlarm=status.outdoorAreaDTFREETimeoutAlarm
            )
}