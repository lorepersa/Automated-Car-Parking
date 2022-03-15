package itunibo.qakobserver

import it.unibo.kactor.ApplMessage

interface IMessageBuilder {
	
	fun buildMessage(input : String, observerName : String) : ApplMessage?
}