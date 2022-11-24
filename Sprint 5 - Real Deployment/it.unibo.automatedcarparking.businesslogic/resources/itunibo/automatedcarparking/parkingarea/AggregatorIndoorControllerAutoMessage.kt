package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorIndoorControllerAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val status = gson.fromJson(input, IndoorAreaStatus::class.java)
		val reserved = status.reserved
		val engagedByCar = status.engagedByCar
		val carEnterTimeoutAlarm = status.carEnterTimeoutAlarm
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_indoorcontroller", "auto_aggregator_indoorcontroller($reserved,$engagedByCar,$carEnterTimeoutAlarm)", observerName)
	}
}