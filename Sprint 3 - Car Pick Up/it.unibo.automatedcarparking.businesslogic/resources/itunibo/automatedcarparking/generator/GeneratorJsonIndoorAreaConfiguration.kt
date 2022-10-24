package itunibo.automatedcarparking.generator

import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.parkingarea.IndoorAreaConfiguration
import java.io.File
import itunibo.automatedcarparking.rolodex.ParkingAreaRolodex

fun generateJsonIndoorAreaConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = IndoorAreaConfiguration(doorName="INDOOR",
		                                        WMAX=500,
		                                        DTOCCUPIED=30000,
		                                        DTCARENTER=30000)
	
	val string = gson.toJson(configuration)
	
	File(ParkingAreaRolodex.filenameIndoorAreaConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonIndoorAreaConfiguration()
}
