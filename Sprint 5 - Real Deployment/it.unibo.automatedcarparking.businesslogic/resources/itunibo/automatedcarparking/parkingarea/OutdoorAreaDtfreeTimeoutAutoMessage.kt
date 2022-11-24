package itunibo.automatedcarparking.parkingarea

import itunibo.qakutils.watchdog.IExpiredTimeAutoMessage
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

object OutdoorAreaDtfreeTimeoutAutoMessage : IExpiredTimeAutoMessage {
	override fun buildMessage(ownerName: String): ApplMessage {
		return MsgUtil.buildEvent(
			ownerName,
			"auto_dtfree_timeout",
			"auto_dtfree_timeout(X)"
		)
	}
} 
