package itunibo.automatedcarparking.transporttrolley

import itunibo.qakutils.watchdog.IExpiredTimeAutoMessage
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

object BackToHomeAutoMessage : IExpiredTimeAutoMessage {
	override fun buildMessage(ownerName: String): ApplMessage {
		return MsgUtil.buildDispatch(
			ownerName,
			"auto_transport_trolley_go_home",
			"auto_transport_trolley_go_home(X)",
			ownerName
		)
	}
} 