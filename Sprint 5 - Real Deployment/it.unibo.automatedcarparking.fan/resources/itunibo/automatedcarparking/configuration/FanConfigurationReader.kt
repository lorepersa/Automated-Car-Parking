package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.fan.FanConfiguration

object FanConfigurationReader {
	
	fun read(filename : String) : FanConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val obj = gson.fromJson<FanConfiguration>(string, FanConfiguration::class.java)
	
		return obj
	}
} 