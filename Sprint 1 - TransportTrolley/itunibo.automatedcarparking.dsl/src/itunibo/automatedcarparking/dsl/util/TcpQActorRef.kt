package itunibo.automatedcarparking.dsl.util

import it.unibo.kactor.sysUtil
import java.net.Socket
import java.util.Scanner
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class TcpQActorRef(val remoteActorName : String, val host : String, val port : Int) : IQActorRef {
	private val client : Socket
	private val scanner : Scanner
	private val writerMutex = Mutex()
	private val readerMutex = Mutex()
	
	init {
		client = Socket(host, port)
		scanner = Scanner(client.inputStream)
	}
	
	fun close() {
		scanner.close()
		client.close()
	}
	
	override suspend fun request(sender : String, msgId : String, msgPayload : String) = withContext(Dispatchers.Default) {
		val message = MsgUtil.buildRequest(sender, msgId, msgPayload, remoteActorName)
        send(message)
	}
	
	override suspend fun reply(sender : String, reqId : String, msgId : String, msgPayload : String) = withContext(Dispatchers.Default) {
		val message = MsgUtil.buildReply(sender, msgId, msgPayload, remoteActorName)
        send(message)
	}
	
	override suspend fun forward(sender : String, msgId : String, msgPayload : String) = withContext(Dispatchers.Default) {
		val message = MsgUtil.buildDispatch(sender, msgId, msgPayload, remoteActorName)
        send(message)
	}
	
	override suspend fun emit(sender : String, msgId : String, msgPayload : String) = withContext(Dispatchers.Default) {
		val message = MsgUtil.buildEvent(sender, msgId, msgPayload)
        send(message)
	}
	
	override suspend fun receive() : Message {
		lateinit var message : Message
		readerMutex.withLock {
			lateinit var line : String
			withContext(Dispatchers.IO) {
				line = scanner.nextLine()
			}
			withContext(Dispatchers.Default) {
				message = Message(line)
			}
		}
		
		return message
    }
	
    private suspend fun send(message : ApplMessage) {
		writerMutex.withLock {
			withContext(Dispatchers.IO) {
				client.outputStream.write("$message\n".toByteArray())
			}
		}
    }
}