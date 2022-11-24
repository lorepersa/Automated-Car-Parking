package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorOutdoorControllerAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val status = gson.fromJson(input, OutdoorAreaStatus::class.java)
		val reserved = status.reserved
		val engagedByCar = status.engagedByCar
		val dtfreeTimeoutAlarm = status.dtfreeTimeoutAlarm
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_outdoorcontroller", "auto_aggregator_outdoorcontroller($reserved,$engagedByCar,$dtfreeTimeoutAlarm)", observerName)
	}
}