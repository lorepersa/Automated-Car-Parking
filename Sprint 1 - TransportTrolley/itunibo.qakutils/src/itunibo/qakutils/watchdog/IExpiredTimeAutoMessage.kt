package itunibo.qakutils.watchdog

import it.unibo.kactor.ApplMessage

interface IExpiredTimeAutoMessage {
	
	fun buildMessage(ownerName : String) : ApplMessage
}