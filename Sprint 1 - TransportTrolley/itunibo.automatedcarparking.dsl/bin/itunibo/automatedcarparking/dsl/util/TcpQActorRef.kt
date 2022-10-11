package itunibo.automatedcarparking.dsl.util

import it.unibo.kactor.sysUtil
import java.net.Socket
import java.util.Scanner
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil

class TcpQActorRef(val remoteActorName : String) : IQActorRef {
	private val client : Socket
	private val scanner : Scanner
	
	init {
		val context = sysUtil.solve("qactor($remoteActorName,CTX,_)", "CTX")!!
		val host = sysUtil.solve("getCtxHost($context,H)", "H")!!
		val port = sysUtil.solve("getCtxPort($context,P)", "P")!!.toInt()
		client = Socket(host, port)
        scanner = Scanner(client.inputStream)
	}
	
	fun close() {
		scanner.close()
		client.close()
	}
	
	override fun request(sender : String, msgId : String, msgPayload : String) {
		val message = MsgUtil.buildRequest(sender, msgId, msgPayload, remoteActorName)
        send(message)
	}
	
	override fun reply(sender : String, reqId : String, msgId : String, msgPayload : String) {
		val message = MsgUtil.buildReply(sender, msgId, msgPayload, remoteActorName)
        send(message)
	}
	
	override fun forward(sender : String, msgId : String, msgPayload : String) {
		val message = MsgUtil.buildDispatch(sender, msgId, msgPayload, remoteActorName)
        send(message)
	}
	
	override fun emit(sender : String, msgId : String, msgPayload : String) {
		val message = MsgUtil.buildEvent(sender, msgId, msgPayload)
        send(message)
	}
	
	override fun receive() : Message {
        val line = scanner.nextLine()
        val message = Message(line)
		return message
    }
	
    private fun send(message : ApplMessage) {
        client.outputStream.write("$message\n".toByteArray())
    }
}