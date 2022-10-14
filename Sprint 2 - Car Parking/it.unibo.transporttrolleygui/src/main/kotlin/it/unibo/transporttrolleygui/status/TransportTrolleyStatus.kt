package it.unibo.transporttrolleygui.status

data class Position(var column : Int,
                    var row : Int,
                    var direction : String)

data class TransportTrolleyStatus(var stopped : Boolean,
                                  var idle : Boolean,
                                  var moveFailed : Boolean,
                                  var coordinate : Position
)