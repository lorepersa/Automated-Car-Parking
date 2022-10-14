package itunibo.automatedcarparking.parkingarea.persistence

import itunibo.automatedcarparking.parkingarea.PersistentParkingSlotCollection
import itunibo.automatedcarparking.parkingarea.StatusFactory

object ParkingSlotCollectionDAO {
	
	var json = false
	
	suspend fun init(mode : String) {
		if (mode.equals("json")) {
			json = true
		} else {
			throw RuntimeException("ParkingSlotCollectionDAO unrecognized mode " + mode)
		}
	
	}
	
	suspend fun read() : PersistentParkingSlotCollection {
		if (json) {
			return ParkingSlotDocument.readAllParkingSlots()
		} else {
			throw RuntimeException("ParkingSlotCollectionDAO not initialized")
		}

	}
	
	suspend fun setFree(slotnum : Int) {
		if (json) {
			ParkingSlotDocument.setFreeParkingSlot(slotnum)
		} else {
			throw RuntimeException("ParkingSlotCollectionDAO not initialized")
		}
	}

	suspend fun setEngaged(slotnum : Int, tokenid : String) {
		if (json) {
			ParkingSlotDocument.setEngagedParkingSlot(slotnum, tokenid)
		} else {
			throw RuntimeException("ParkingSlotCollectionDAO not initialized")
		}
	}

}