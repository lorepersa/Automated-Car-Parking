package itunibo.automatedcarparking.dsl.util

import it.unibo.kactor.sysUtil

class QActorRef(val actorName : String) : IQActorRef {
	
	val tcpActor : TcpQActorRef
	
	init {
		val context = sysUtil.solve("qactor($actorName,CTX,_)", "CTX")!!
		val host = sysUtil.solve("getCtxHost($context,H)", "H")!!
		val port = sysUtil.solve("getCtxPort($context,P)", "P")!!.toInt()
		
		tcpActor = TcpQActorRef(actorName, host, port)
	}
	
	override suspend fun request(sender : String, msgId : String, msgPayload : String) {
		tcpActor.request(sender, msgId, msgPayload)
	}
	
	override suspend fun reply(sender : String, reqId : String, msgId : String, msgPayload : String) {
		tcpActor.reply(sender, reqId, msgId, msgPayload) 
	}
	
	override suspend fun forward(sender : String, msgId : String, msgPayload : String) {
		tcpActor.forward(sender, msgId, msgPayload)
	}
	
	override suspend fun emit(sender : String, msgId : String, msgPayload : String) {
		tcpActor.emit(sender, msgId, msgPayload)
	}
	
	override suspend fun receive() : Message {
		return tcpActor.receive()
	}
}