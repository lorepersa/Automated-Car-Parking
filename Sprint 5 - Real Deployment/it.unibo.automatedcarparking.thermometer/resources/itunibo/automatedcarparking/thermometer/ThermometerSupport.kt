package itunibo.automatedcarparking.thermometer

import it.unibo.kactor.ActorBasic
import itunibo.automatedcarparking.configuration.ThermometerConfigurationReader
import itunibo.automatedcarparking.rolodex.ThermometerRolodex

class ThermometerSupport(owner : ActorBasic) {
	
	init {
		val configuration = ThermometerConfigurationReader.read(ThermometerRolodex.filenameConfiguration)
		
		val mode = configuration.mode.toLowerCase()
		when (mode) {
			"virtual" -> {
				// do nothing
			}
			"real" -> {
				RealThermometerSupport.register(owner)
			}
			else -> {
				throw Exception("Unknown Thermometer mode: $mode")
			}
		}
	
	
	}
	
}