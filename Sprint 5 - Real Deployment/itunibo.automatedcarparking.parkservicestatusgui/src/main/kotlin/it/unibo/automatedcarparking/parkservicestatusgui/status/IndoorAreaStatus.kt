package it.unibo.automatedcarparking.parkservicestatusgui.status

data class IndoorAreaStatus(var weightsensorOn : Boolean = false,
                            var indoorAreaWeight : Int = 0,
                            var indoorAreaReserved : Boolean = false,
                            var indoorAreaEngaged : Boolean = false,
                            var indoorAreaCarEnterTimeoutAlarm : Boolean = false) {
    constructor(status : AutomatedCarParkingStatus) :
            this(weightsensorOn=status.weightsensorOn,
                indoorAreaWeight=status.indoorAreaWeight,
                indoorAreaReserved=status.indoorAreaReserved,
                indoorAreaEngaged=status.indoorAreaEngaged,
                indoorAreaCarEnterTimeoutAlarm=status.indoorAreaCarEnterTimeoutAlarm
            )
}