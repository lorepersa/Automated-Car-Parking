package itunibo.qakobserver

import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

object DefaultMessageBuilder : IMessageBuilder {
	
	override fun buildMessage(input : String, observerName : String) : ApplMessage? {
		return MsgUtil.buildDispatch(observerName, "update", "update($input)", observerName)
	}
}