package itunibo.automatedcarparking.configuration

import java.io.File
import com.google.gson.Gson
import itunibo.automatedcarparking.outsonar.OutSonarConfiguration

object OutSonarConfigurationReader {
	
	fun read(filename : String) : OutSonarConfiguration {
		val string = File(filename).readText(Charsets.UTF_8)
		// json
		val gson = Gson()
		val timer = gson.fromJson<OutSonarConfiguration>(string, OutSonarConfiguration::class.java)
	
		return timer
	}
} 