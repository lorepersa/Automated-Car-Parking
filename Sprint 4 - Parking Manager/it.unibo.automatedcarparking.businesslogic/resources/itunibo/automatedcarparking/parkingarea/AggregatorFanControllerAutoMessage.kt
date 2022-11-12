package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorFanControllerAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val status = gson.fromJson(input, FanControllerStatus::class.java)
		val on = status.on
		val automatic = status.automatic
		val failureReason = status.failureReason
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_fancontroller", "auto_aggregator_fancontroller($on,$automatic,$failureReason)", observerName)
	}
}