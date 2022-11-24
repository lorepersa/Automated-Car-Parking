package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorWeightSensorAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val weight = gson.fromJson(input, Weight::class.java).weight
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_weightsensor", "auto_aggregator_weightsensor($weight)", observerName)
	}
}