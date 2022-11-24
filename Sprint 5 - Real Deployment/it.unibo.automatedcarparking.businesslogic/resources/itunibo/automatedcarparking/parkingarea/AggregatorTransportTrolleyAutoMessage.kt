package itunibo.automatedcarparking.parkingarea

import itunibo.qakobserver.IMessageBuilder
import com.google.gson.Gson
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import itunibo.automatedcarparking.transporttrolley.TransportTrolleyStatus

object AggregatorTransportTrolleyAutoMessage : IMessageBuilder {
	val gson = Gson()
	override fun buildMessage(input : String, observerName : String) : ApplMessage {
		val status = gson.fromJson(input, TransportTrolleyStatus::class.java)
		val stopped = status.stopped
		val idle = status.idle
		val moveFailed = status.moveFailed
		val column = status.coordinate.column
		val row = status.coordinate.row
		val direction = status.coordinate.direction
		return MsgUtil.buildDispatch(observerName, "auto_aggregator_transporttrolley", "auto_aggregator_transporttrolley($stopped,$idle,$moveFailed,$column,$row,$direction)", observerName)
	}
}