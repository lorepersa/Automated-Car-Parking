package itunibo.automatedcarparking.fan

import it.unibo.kactor.ActorBasic
import itunibo.automatedcarparking.configuration.FanConfigurationReader
import itunibo.automatedcarparking.rolodex.FanRolodex

class FanSupport(val owner : ActorBasic) {
	
	var mode : String
	
	init {
		val configuration = FanConfigurationReader.read(FanRolodex.filenameConfiguration)
		
		mode = configuration.mode.toLowerCase()
	}
	
	suspend fun turnOn() {
		when (mode) {
			"virtual" -> {
				owner.autoMsg("auto_fan_done", "auto_fan_done(X)")
			}
			"real" -> {
				RealFanSupport.turnOn(owner)
			}
			else -> {
				throw Exception("Unknown Fan mode: $mode")
			}
		}
	}
	
	suspend fun turnOff() {
		when (mode) {
			"virtual" -> {
				owner.autoMsg("auto_fan_done", "auto_fan_done(X)")
			}
			"real" -> {
				RealFanSupport.turnOff(owner)
			}
			else -> {
				throw Exception("Unknown Fan mode: $mode")
			}
		}
	}
	
}