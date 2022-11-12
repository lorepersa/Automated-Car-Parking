package it.unibo.automatedcarparking.parkservicegui.qakutil

import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import java.net.Socket
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class RemoteQakContext(hostname : String, port : Int, private val actorNamePrefix : String) {
    private val client = Socket(hostname, port)
    private val scanner = Scanner(client.inputStream)
    private val socketLock = ReentrantLock()
    private val listLock = ReentrantLock()
    private val listCondition = listLock.newCondition()
    private val messageList : MutableList<Message> = mutableListOf()
    private val nameList : MutableList<String> = mutableListOf()

    init {
        thread(start = true, isDaemon = true) {
            while (true) {
                val line = scanner.nextLine()

                val message = Message(line)

                try {
                    listLock.lock()

                    messageList.add(message)
                    listCondition.signalAll()
                } finally {
                    listLock.unlock()
                }
            }
        }
    }

    private fun getRandomActorName() : String {
        try {
            listLock.lock()
            val allowedChars = ('a'..'z')
            while (true) {
                val name = (1..5).map { allowedChars.random() }.joinToString(separator = "", prefix = actorNamePrefix)

                if (nameList.contains(name)) {
                    continue
                }

                return name
            }
        } finally {
            listLock.unlock()
        }
    }

    private fun send(message : ApplMessage) {
        try {
            socketLock.lock()
            //println("sending $message...")
            client.outputStream.write("$message\n".toByteArray())
        } finally {
            socketLock.unlock()
        }
    }

    private fun receive(localActor: String, remoteActor: String) : Message {
        var message: Message?
        try {
            listLock.lock()
            while (true) {
                message = messageList.firstOrNull {
                    it.getSender() == remoteActor && it.getReceiver() == localActor
                }

                if (message != null) {
                    messageList.remove(message)
                    nameList.remove(localActor)
                    break
                }

                listCondition.await()
            }
        } finally {
            listLock.unlock()
        }

        return message!!
    }

    fun syncRequest(msgId : String, payload : String, actorName : String) : Message {
        val sender = getRandomActorName()
        val message = MsgUtil.buildRequest(sender, msgId, "$msgId($payload)", actorName)
        send(message)
        return receive(sender, actorName)
    }

    fun sendDispatch(msgId : String, payload : String, actorName : String) {
        val message = MsgUtil.buildDispatch(actorNamePrefix, msgId, "$msgId($payload)", actorName)
        send(message)
    }

}