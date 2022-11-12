package itunibo.automatedcarparking.parkingarea.persistence

import itunibo.automatedcarparking.parkingarea.Status
import com.google.gson.GsonBuilder
import itunibo.automatedcarparking.rolodex.ParkingAreaRolodex
import java.io.File
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import itunibo.automatedcarparking.parkingarea.PersistentParkingSlotCollection
import itunibo.automatedcarparking.parkingarea.StatusFactory

object ParkingSlotDocument {
	
	private lateinit var collection : PersistentParkingSlotCollection
	private val gson = GsonBuilder().setPrettyPrinting().create()
	private val file = File(ParkingAreaRolodex.filenameParkingSlotDatabase)
	
	suspend fun setFreeParkingSlot(slotnum : Int) {
		val persistentParkingSlot = collection.parkingSlots.find { it.parkingSlot.number == slotnum }
		
		persistentParkingSlot?.let {
			val parkingSlot = persistentParkingSlot.parkingSlot
			persistentParkingSlot.tokenid = ""
			parkingSlot.status = StatusFactory.getFreeStatus()
				
			val string = gson.toJson(collection)
			withContext(Dispatchers.IO) {
				file.printWriter().use { out ->
					out.print(string)
					out.flush()
				}
			}
		}
	}
	
	suspend fun setEngagedParkingSlot(slotnum : Int, tokenid : String) {
		val persistentParkingSlot = collection.parkingSlots.find { it.parkingSlot.number == slotnum }
		
		persistentParkingSlot?.let {
			val parkingSlot = persistentParkingSlot.parkingSlot
			persistentParkingSlot.tokenid = tokenid
			parkingSlot.status = StatusFactory.getEngagedStatus()
				
			val string = gson.toJson(collection)
			withContext(Dispatchers.IO) {
				file.printWriter().use { out ->
					out.print(string)
					out.flush()
				}
			}
		}
	}
	
	suspend fun writeAllParkingSlots(collection : PersistentParkingSlotCollection) {
		this.collection = collection
		val string = gson.toJson(collection)
		withContext(Dispatchers.IO) {
			file.printWriter().use { out ->
					out.print(string)
					out.flush()
			}
		}
	}
	
	suspend fun readParkingSlotStatus(slotnum : Int) : Status? {
		
		// read only cached values
		val persistentParkingSlot = collection.parkingSlots.find { it.parkingSlot.number == slotnum }
		
		if (persistentParkingSlot != null) {
			return persistentParkingSlot.parkingSlot.status
		}
		
		return null
		
	}
	
	suspend fun readAllParkingSlots() : PersistentParkingSlotCollection {
		lateinit var string : String
		
		withContext(Dispatchers.IO) {
			string = file.readText(Charsets.UTF_8)
		}
		collection = gson.fromJson<PersistentParkingSlotCollection>(string, PersistentParkingSlotCollection::class.java)
		
		return collection
	}
}