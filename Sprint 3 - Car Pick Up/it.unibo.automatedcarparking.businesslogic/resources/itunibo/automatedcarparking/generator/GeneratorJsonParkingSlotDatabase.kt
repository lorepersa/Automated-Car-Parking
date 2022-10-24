package itunibo.automatedcarparking.generator

import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.parkingarea.ParkingSlotCollection
import itunibo.automatedcarparking.rolodex.ParkingAreaRolodex
import java.io.File
import itunibo.automatedcarparking.parkingarea.ParkingSlot
import itunibo.automatedcarparking.parkingarea.StatusFactory
import itunibo.automatedcarparking.parkingarea.PersistentParkingSlot
import itunibo.automatedcarparking.parkingarea.PersistentParkingSlotCollection

fun generateJsonParkingSlotDatabase() {
	// json 
	val gson = GsonBuilder().setPrettyPrinting().create()
	
	val collection = mutableSetOf<PersistentParkingSlot>()
	
	for (i in 1..6) {
		collection.add(PersistentParkingSlot(ParkingSlot(i, StatusFactory.getFreeStatus()), ""))
	}
	
	val parkingSlots = PersistentParkingSlotCollection(collection)
	
	val string = gson.toJson(parkingSlots)
	
	File(ParkingAreaRolodex.filenameParkingSlotDatabase).writeText(string, Charsets.UTF_8)
	
	println(string)
}

fun main() {
	generateJsonParkingSlotDatabase()
}