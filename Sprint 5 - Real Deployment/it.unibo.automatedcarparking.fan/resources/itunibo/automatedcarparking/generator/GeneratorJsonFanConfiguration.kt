package itunibo.automatedcarparking.generator

import itunibo.automatedcarparking.fan.FanConfiguration
import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.rolodex.FanRolodex
import java.io.File

fun generateJsonFanConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = FanConfiguration(mode="virtual")
	
	val string = gson.toJson(configuration)
	
	File(FanRolodex.filenameConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonFanConfiguration()
}