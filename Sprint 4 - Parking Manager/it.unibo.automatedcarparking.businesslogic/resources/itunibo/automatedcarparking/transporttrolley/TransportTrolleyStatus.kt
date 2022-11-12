package itunibo.automatedcarparking.transporttrolley

data class TransportTrolleyStatus(var stopped : Boolean,
								  var idle : Boolean,
								  var moveFailed : Boolean,
								  var coordinate : Position) {
	
	fun getStatusIdleWorkingStopped() : String {
		if (stopped) {
			return "STOPPED"
		} else if (idle) {
			return "IDLE"
		} else {
			return "WORKING"
		}
	}
}