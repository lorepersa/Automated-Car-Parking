package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.parkingarea.IndoorAreaConfiguration

object IndoorAreaConfigurationReader {
	
	fun read(filename : String) : IndoorAreaConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val timer = gson.fromJson<IndoorAreaConfiguration>(string, IndoorAreaConfiguration::class.java)
	
		return timer
	}
} 