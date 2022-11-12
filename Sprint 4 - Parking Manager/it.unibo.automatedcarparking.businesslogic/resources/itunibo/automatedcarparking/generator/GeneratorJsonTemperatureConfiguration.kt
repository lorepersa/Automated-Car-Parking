package itunibo.automatedcarparking.generator

import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.rolodex.ParkingAreaRolodex
import itunibo.automatedcarparking.parkingarea.TemperatureConfiguration
import java.io.File

fun generateJsonTemperatureConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = TemperatureConfiguration(TMAX=35)
	
	val string = gson.toJson(configuration)
	
	File(ParkingAreaRolodex.filenameTemperatureConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonTemperatureConfiguration()
}

