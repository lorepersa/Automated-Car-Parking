package itunibo.automatedcarparking.parkingarea

import com.google.gson.Gson
import itunibo.automatedcarparking.parkingarea.persistence.ParkingSlotCollectionDAO

// parking slot permitted life cycle:
// free -> reserved -> engaged
// free -> reserved -> free
// engaged -> free

object ParkingSlotController {
	
	private var totalNumberOfParkingSlots = 0
	private val freeParkingSlots = mutableListOf<Int>()
	private val reservedParkingSlots = mutableMapOf<Int,TokenId>()
	private val engagedParkingSlots = mutableMapOf<TokenId,Int>()
	private val statusParkingSlots = mutableSetOf<ParkingSlot>()
	private val gson = Gson()
	
	suspend fun init() {
		ParkingSlotCollectionDAO.init("json")
		
		val collection = ParkingSlotCollectionDAO.read()
		
		collection.parkingSlots.forEach {
			val slotnum = it.parkingSlot.number
			val status = it.parkingSlot.status
			val tokenid = it.tokenid
			
			if (status.isFree()) {
				freeParkingSlots.add(slotnum)
				statusParkingSlots.add(ParkingSlot(slotnum, StatusFactory.getFreeStatus()))
			} else if (status.isEngaged()) {
				engagedParkingSlots.put(TokenId(tokenid), slotnum)
				statusParkingSlots.add(ParkingSlot(slotnum, StatusFactory.getEngagedStatus()))
			}
		}
	}
	
	fun getJsonStatus() : String {
		return gson.toJson(statusParkingSlots)
	}
	
	fun isAvailableParkingSlot() : Boolean {
		return !freeParkingSlots.isEmpty()
	}
	
	fun moveFromFreeToReserved() : Pair<Int,String> {
		
		if (isAvailableParkingSlot()) {
			val slotnum = freeParkingSlots.random()
			
			freeParkingSlots.remove(slotnum)
			
			val tokenId = TokenId(slotnum)
			reservedParkingSlots.put(slotnum, tokenId)
			
			val parkingslot = statusParkingSlots.find { parkingslot -> parkingslot.number == slotnum }!!
			parkingslot.status = StatusFactory.getReservedStatus()
			
			return Pair(slotnum, tokenId.token)
		}
		
		return Pair(0, "")
	}
	
	fun moveFromReservedToFree(slotnum : Int) {
		
		reservedParkingSlots.remove(slotnum)?.let {
			freeParkingSlots.add(slotnum)
			
			val parkingslot = statusParkingSlots.find { parkingslot -> parkingslot.number == slotnum }!!
			parkingslot.status = StatusFactory.getFreeStatus()
		}
		
	}
	
	suspend fun moveFromReservedToEngaged(slotnum : Int) {
		val tokenid = reservedParkingSlots.remove(slotnum)
		tokenid?.let {
			engagedParkingSlots.put(it, slotnum)
			
			val parkingslot = statusParkingSlots.find { parkingslot -> parkingslot.number == slotnum }!!
			parkingslot.status = StatusFactory.getEngagedStatus()
			
			ParkingSlotCollectionDAO.setEngaged(slotnum, tokenid.token)
		}
	}
	
	suspend fun reset() {
		for (slotnum in reservedParkingSlots.keys) {
			freeParkingSlots.add(slotnum)
		}
		reservedParkingSlots.clear()
		
		for (tokenid in engagedParkingSlots.keys) {
			val slotnum = engagedParkingSlots.get(tokenid)
			slotnum?.let {
				freeParkingSlots.add(slotnum)
			}
		}
		engagedParkingSlots.clear()
				
		for (slotnum in freeParkingSlots) {
			ParkingSlotCollectionDAO.setFree(slotnum)
		}
	}
	
	suspend fun moveFromEngagedToFree(tokenId : TokenId) {
		
		engagedParkingSlots.remove(tokenId)?.let { slotnum ->
			freeParkingSlots.add(slotnum)
			val parkingslot = statusParkingSlots.find { parkingslot -> parkingslot.number == slotnum }!!
			parkingslot.status = StatusFactory.getFreeStatus()
			
			ParkingSlotCollectionDAO.setFree(slotnum)
		}
	}
	
	suspend fun moveFromEngagedToFree(tokenId : String) {
		moveFromEngagedToFree(TokenId(tokenId))
	}
	
	fun checkTokenId(tokenId : TokenId) : Int {
		engagedParkingSlots.get(tokenId)?.let { slotnum ->
			return slotnum
		}
		
		return -1
	}
	
	fun checkTokenId(tokenId : String) : Int {
		return checkTokenId(TokenId(tokenId))
	}
	
	
}
