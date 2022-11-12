package it.unibo.automatedcarparking.parkservicestatusgui.status

data class ParkingSlotsStatus(var parkingSlotsStatus : MutableSet<ParkingSlot> = mutableSetOf()) {
    constructor(status : AutomatedCarParkingStatus) :
            this(parkingSlotsStatus=status.parkingSlotsStatus)
}