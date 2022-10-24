package itunibo.automatedcarparking.parkingarea

import itunibo.qakutils.watchdog.IExpiredTimeAutoMessage
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

object IndoorAreaDtcarenterTimeoutAutoMessage : IExpiredTimeAutoMessage {
	override fun buildMessage(ownerName: String): ApplMessage {
		return MsgUtil.buildEvent(
			ownerName,
			"auto_dtcarenter_timeout",
			"auto_dtcarenter_timeout(X)"
		)
	}
} 