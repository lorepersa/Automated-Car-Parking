package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.parkingarea.TemperatureConfiguration

object TemperatureConfigurationReader {
	
	fun read(filename : String) : TemperatureConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val obj = gson.fromJson<TemperatureConfiguration>(string, TemperatureConfiguration::class.java)
	
		return obj
	}
} 