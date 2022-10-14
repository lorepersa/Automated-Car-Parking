package itunibo.automatedcarparking.parkingarea

object StatusFactory {
	
	val free = Status("FREE")
	val engaged = Status("ENGAGED")
	val reserved = Status("RESERVED")
	
	fun getFreeStatus() : Status {
		return free
	}
	
	fun getEngagedStatus() : Status {
		return engaged
	}
	
	fun getReservedStatus() : Status {
		return reserved
	}
}