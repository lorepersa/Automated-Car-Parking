package itunibo.qakutils.watchdog

import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

object DefaultMessageBuilder : IExpiredTimeAutoMessage {
	
	override fun buildMessage(ownerName : String) : ApplMessage {
		return MsgUtil.buildDispatch(ownerName, "time_expired", "time_expired(X)", ownerName)
	}
}