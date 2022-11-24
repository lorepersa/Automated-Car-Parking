package itunibo.automatedcarparking.generator

import itunibo.automatedcarparking.thermometer.ThermometerConfiguration
import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.rolodex.ThermometerRolodex
import java.io.File

fun generateJsonThermometerConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = ThermometerConfiguration(mode="virtual")
	
	val string = gson.toJson(configuration)
	
	File(ThermometerRolodex.filenameConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonThermometerConfiguration()
}