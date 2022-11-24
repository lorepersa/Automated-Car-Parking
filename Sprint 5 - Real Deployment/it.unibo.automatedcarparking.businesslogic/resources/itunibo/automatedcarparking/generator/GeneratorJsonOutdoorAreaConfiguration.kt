package itunibo.automatedcarparking.generator

import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.parkingarea.OutdoorAreaConfiguration
import java.io.File
import itunibo.automatedcarparking.rolodex.ParkingAreaRolodex

fun generateJsonOutdoorAreaConfiguration() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val configuration = OutdoorAreaConfiguration(doorName="OUTDOOR",
                        DMIN=200,
                        DTFREE=30000)
	
	val string = gson.toJson(configuration)
	
	File(ParkingAreaRolodex.filenameOutdoorAreaConfiguration).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonOutdoorAreaConfiguration()
}

