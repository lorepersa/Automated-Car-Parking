package itunibo.automatedcarparking.parkingarea

import itunibo.qakutils.watchdog.IExpiredTimeAutoMessage
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

object IndoorAreaDtoccupiedTimeoutAutoMessage : IExpiredTimeAutoMessage {
	override fun buildMessage(ownerName: String): ApplMessage {
		return MsgUtil.buildEvent(
			ownerName,
			"auto_dtoccupied_timeout",
			"auto_dtoccupied_timeout(X)"
		)
	}
} 