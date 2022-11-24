package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorOutSonarAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val distance = gson.fromJson(input, Distance::class.java).distance
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_outsonar", "auto_aggregator_outsonar($distance)", observerName)
	}
}