package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorParkingSlotsControllerAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val itemType = object : com.google.gson.reflect.TypeToken<HashSet<ParkingSlot>>() {}.type 
		val parkingSlotsStatus : HashSet<ParkingSlot> = gson.fromJson<HashSet<ParkingSlot>>(input, itemType)
		var P1 = ""
		var P2 = ""
		var P3 = ""
		var P4 = ""
		var P5 = ""
		var P6 = ""
		for (parkingSlot in parkingSlotsStatus) {
			if (parkingSlot.number == 1) {
				P1 = parkingSlot.status.status
			}
			else if (parkingSlot.number == 2) {
				P2 = parkingSlot.status.status
			}
			else if (parkingSlot.number == 3) {
				P3 = parkingSlot.status.status
			}
			else if (parkingSlot.number == 4) {
				P4 = parkingSlot.status.status
			}
			else if (parkingSlot.number == 5) {
				P5 = parkingSlot.status.status
			}
			else if (parkingSlot.number == 6) {
				P6 = parkingSlot.status.status
			}
		}
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_parkingslotscontroller", "auto_aggregator_parkingslotscontroller($P1,$P2,$P3,$P4,$P5,$P6)", observerName)
	}
}