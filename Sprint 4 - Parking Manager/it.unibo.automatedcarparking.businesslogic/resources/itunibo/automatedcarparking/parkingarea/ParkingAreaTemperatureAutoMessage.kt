package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object ParkingAreaTemperatureAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val temperature = gson.fromJson(input, Temperature::class.java).temperature
		return MsgUtil.buildDispatch(observerName, "auto_parking_area_temperature", "auto_parking_area_temperature($temperature)", observerName)
	}
}