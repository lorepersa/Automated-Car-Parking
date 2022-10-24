package itunibo.automatedcarparking.configuration

import com.google.gson.Gson
import java.io.File
import itunibo.automatedcarparking.transporttrolley.TransportTrolleyConfiguration

object TransportTrolleyConfigurationReader {
	
	fun read(filename : String) : TransportTrolleyConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val timer = gson.fromJson<TransportTrolleyConfiguration>(string, TransportTrolleyConfiguration::class.java)
	
		return timer
	}
} 