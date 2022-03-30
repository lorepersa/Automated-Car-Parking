package it.unibo.automatedcarparking.sprint1.gui.qakutil

import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import java.net.Socket
import java.util.*

class QakTcpClient(val hostname : String, val port : Int, val context : String, val actorName : String) {

    private lateinit var client : Socket
    private lateinit var scanner : Scanner

    fun connect() {
        client = Socket(hostname, port)
        scanner = Scanner(client.inputStream)
    }

    fun close() {
        client.close()
    }

    private fun send(message : ApplMessage) {
        println("sending $message...")
        client.outputStream.write("$message\n".toByteArray())
    }

    fun syncRequest(sender : String, msgId : String, payload : String) : Message {
        val message = MsgUtil.buildRequest(sender, msgId, "$msgId($payload)", actorName)
        send(message)
        return receive()
    }

    fun sendDispatch(sender : String, msgId : String, payload : String) {
        val message = MsgUtil.buildDispatch(sender, msgId, "$msgId($payload)", actorName)
        send(message)
    }

    fun receive() : Message {
        val line = scanner.nextLine()

        return Message(line)
    }
}