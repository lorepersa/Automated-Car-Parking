package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.thermometer.ThermometerConfiguration

object ThermometerConfigurationReader {
	
	fun read(filename : String) : ThermometerConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val obj = gson.fromJson<ThermometerConfiguration>(string, ThermometerConfiguration::class.java)
	
		return obj
	}
} 