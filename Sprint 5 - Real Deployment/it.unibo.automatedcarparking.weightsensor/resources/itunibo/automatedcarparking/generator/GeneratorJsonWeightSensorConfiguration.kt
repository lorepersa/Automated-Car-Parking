package itunibo.automatedcarparking.generator

import itunibo.automatedcarparking.weightsensor.WeightSensorConfiguration
import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.rolodex.WeightSensorRolodex
import java.io.File

fun generateJsonWeightSensorConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = WeightSensorConfiguration(mode="virtual")
	
	val string = gson.toJson(configuration)
	
	File(WeightSensorRolodex.filenameConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonWeightSensorConfiguration()
}