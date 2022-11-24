package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

object AggregatorThermometerAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val temperature = gson.fromJson(input, Temperature::class.java).temperature
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_thermometer", "auto_aggregator_thermometer($temperature)", observerName)
	}
}