package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.weightsensor.WeightSensorConfiguration

object WeightSensorConfigurationReader {
	
	fun read(filename : String) : WeightSensorConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val timer = gson.fromJson<WeightSensorConfiguration>(string, WeightSensorConfiguration::class.java)
	
		return timer
	}
} 