package itunibo.automatedcarparking.parkingarea

import itunibo.automatedcarparking.transporttrolley.Position

data class AggregateStatus(var weightsensorOn : Boolean = false,
                           var indoorAreaWeight : Int = 0,
                           var indoorAreaReserved : Boolean = false,
                           var indoorAreaEngaged : Boolean = false,
                           var indoorAreaCarEnterTimeoutAlarm : Boolean = false,
                           var outsonarOn : Boolean = false,
                           var outdoorAreaDistance : Int = 0,
                           var outdoorAreaReserved : Boolean = false,
                           var outdoorAreaEngaged : Boolean = false,
                           var outdoorAreaDTFREETimeoutAlarm : Boolean = false,
                           var parkingAreaTemperature : Int = 0,
                           var parkingAreaTemperatureHigh : Boolean = false,
                           var fanOn : Boolean = false,
                           var fanAutomatic : Boolean = false,
                           var fanFailureReason : String = "",
                           var transportTrolleyStopped : Boolean = false,
                           var transportTrolleyIdle : Boolean = true,
                           var transportTrolleyMoveFailed : Boolean = false,
                           var transportTrolleyCoordinate : Position = Position(0,0, "downDir"),
                           var parkingSlotsStatus : MutableSet<ParkingSlot> = mutableSetOf<ParkingSlot>(),
                           )
