package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.parkingarea.OutdoorAreaConfiguration

object OutdoorAreaConfigurationReader {
	
	fun read(filename : String) : OutdoorAreaConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val obj = gson.fromJson<OutdoorAreaConfiguration>(string, OutdoorAreaConfiguration::class.java)
	
		return obj
	}
} 