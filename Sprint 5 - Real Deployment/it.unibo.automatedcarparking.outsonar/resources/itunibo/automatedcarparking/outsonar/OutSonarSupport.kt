package itunibo.automatedcarparking.outsonar

import it.unibo.kactor.ActorBasic
import itunibo.automatedcarparking.configuration.OutSonarConfigurationReader
import itunibo.automatedcarparking.rolodex.OutSonarRolodex

class OutSonarSupport(owner : ActorBasic) {

        private var virtual = true
	
	init {
		val configuration = OutSonarConfigurationReader.read(OutSonarRolodex.filenameConfiguration)
		
		val mode = configuration.mode.toLowerCase()
		when (mode) {
			"virtual" -> {
				virtual = true
			}
			"real" -> {
			        virtual = false
				RealOutSonarSupport.initialize()
				RealOutSonarSupport.register(owner)
			}
			else -> {
				throw Exception("Unknown OutSonar mode: $mode")
			}
		}
	
	
	}
	
	fun isVirtual() : Boolean {
	    return virtual
	}
	
}
