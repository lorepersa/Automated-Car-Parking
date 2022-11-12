package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object ParkingAreaTemperatureLevelAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val status = gson.fromJson(input, TemperatureStatus::class.java)
		val high = status.high
		return MsgUtil.buildDispatch(observerName, "auto_parking_area_temperature_level", "auto_parking_area_temperature_level($high)", observerName)
	}
}