package it.unibo.automatedcarparking.parkservicestatusgui.status

data class TransportTrolleyStatus(var transportTrolleyStopped : Boolean = false,
                                  var transportTrolleyIdle : Boolean = true,
                                  var transportTrolleyMoveFailed : Boolean = false,
                                  var transportTrolleyCoordinate : Position = Position(0,0, "downDir")) {
    constructor(status : AutomatedCarParkingStatus) :
            this(transportTrolleyStopped=status.transportTrolleyStopped,
                transportTrolleyIdle=status.transportTrolleyIdle,
                transportTrolleyMoveFailed=status.transportTrolleyMoveFailed,
                transportTrolleyCoordinate=status.transportTrolleyCoordinate
            )
}