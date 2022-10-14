package itunibo.automatedcarparking.generator

import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.transporttrolley.TransportTrolleyConfiguration
import itunibo.automatedcarparking.rolodex.TransportTrolleyRolodex
import java.io.File

fun generateJsonTransportTrolleyConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = TransportTrolleyConfiguration(timeLimitBackToHome=1000,
		                                              updatePositionAfterEachStep=false,
	                                                  stepMoveDuration=350)
	
	val string = gson.toJson(configuration)
	
	File(TransportTrolleyRolodex.filenameConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonTransportTrolleyConfiguration()
}
