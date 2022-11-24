package it.unibo.automatedcarparking.parkservicestatusgui.status

data class FanStatus(var fanOn : Boolean = false,
                     var fanAutomatic : Boolean = false,
                     var fanFailureReason : String = "") {
    constructor(status : AutomatedCarParkingStatus) :
            this(fanOn=status.fanOn,
                fanAutomatic=status.fanAutomatic,
                fanFailureReason=status.fanFailureReason
            )
}