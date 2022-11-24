package itunibo.automatedcarparking.weightsensor

import it.unibo.kactor.ActorBasic
import itunibo.automatedcarparking.configuration.WeightSensorConfigurationReader
import itunibo.automatedcarparking.rolodex.WeightSensorRolodex

class WeightSensorSupport(owner : ActorBasic) {
	
	init {
		val configuration = WeightSensorConfigurationReader.read(WeightSensorRolodex.filenameConfiguration)
		
		val mode = configuration.mode.toLowerCase()
		when (mode) {
			"virtual" -> {
				// do nothing
			}
			"real" -> {
				RealWeightSensorSupport.register(owner)
			}
			else -> {
				throw Exception("Unknown WeightSensor mode: $mode")
			}
		}
	
	
	}
	
}