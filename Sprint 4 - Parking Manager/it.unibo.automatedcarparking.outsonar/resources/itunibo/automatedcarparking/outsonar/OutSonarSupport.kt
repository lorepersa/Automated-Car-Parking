package itunibo.automatedcarparking.outsonar

import it.unibo.kactor.ActorBasic
import itunibo.automatedcarparking.configuration.OutSonarConfigurationReader
import itunibo.automatedcarparking.rolodex.OutSonarRolodex

class OutSonarSupport(owner : ActorBasic) {
	
	init {
		val configuration = OutSonarConfigurationReader.read(OutSonarRolodex.filenameConfiguration)
		
		val mode = configuration.mode.toLowerCase()
		when (mode) {
			"virtual" -> {
				// do nothing
			}
			"real" -> {
				RealOutSonarSupport.register(owner)
			}
			else -> {
				throw Exception("Unknown OutSonar mode: $mode")
			}
		}
	
	
	}
	
}