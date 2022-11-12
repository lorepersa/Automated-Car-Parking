package it.unibo.automatedcarparking.parkservicestatusgui.status

data class Status(val status : String) {
	init {
		require(status.equals("FREE")
				|| status.equals("ENGAGED")
				|| status.equals("RESERVED")) { "Unknown status: $status" }
	}
	
	fun isFree() : Boolean {
		return status.equals("FREE")
	}
	
	fun isEngaged() : Boolean {
		return status.equals("ENGAGED")
	}
	
	fun isReserved() : Boolean {
		return status.equals("RESERVED")
	}
}

