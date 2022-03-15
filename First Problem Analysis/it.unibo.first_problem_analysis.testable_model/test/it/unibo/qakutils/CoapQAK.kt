package it.unibo.qakutils

import it.unibo.kactor.MsgUtil
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.QakContext
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapHandler
import kotlinx.coroutines.channels.Channel
import org.eclipse.californium.core.coap.CoAP
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope


class CoapResource(var coapQak: CoapQAK) : CoapHandler {

	override fun onLoad(response: CoapResponse) {
		if (response.code == CoAP.ResponseCode.NOT_FOUND)
			return

		val payload = response.responseText

		runBlocking {
			coapQak.updateResource(payload)
		}
	}

	override fun onError() {
		println("ERROR CoapChannel ")
	}
}

class CoapQAK {

	private var coapAddress: String
	private var qakName: String
	private var coapClient: CoapClient
	private var channel = Channel<String>()
	private var coapResource = CoapResource(this)
	private var resource: String = ""

	constructor(address: String, port: Int, context: String, name: String) {
		coapAddress = "coap://$address:$port/$context/$name"
		qakName = name
		coapClient = CoapClient(coapAddress)
		coapClient.observeAndWait(coapResource)
	}

	@Synchronized
	fun updateResource(newResource: String) {
		resource = newResource
	}

	@Synchronized
	fun readResource(): String {
		return resource
	}

	fun getName(): String {
		return qakName
	}

	suspend fun timedRequest(
		sender: String,
		messageName: String,
		payload: String,
		timeLimitMilliseconds: Long
	): Message {

		lateinit var message: Message

		coroutineScope {
			launch(Dispatchers.IO) {
				val client = CoapClient(coapAddress)

				val req = MsgUtil.buildRequest(sender, messageName, "$messageName($payload)", qakName)
				client.timeout = timeLimitMilliseconds
				var reply = client.put(req.toString(), MediaTypeRegistry.TEXT_PLAIN)

				client.shutdown()

				if (reply != null && !reply.isSuccess()) message = Message(null)

				message = Message(reply)
			}
		}

		return message
	}

	suspend fun syncRequest(sender: String, messageName: String, payload: String): Message {
		return timedRequest(sender, messageName, payload, 0) // wait until reply (no time limit)
	}

	suspend fun sendDispatch(sender: String, messageName: String, payload: String) {

		coroutineScope {
			launch(Dispatchers.IO) {
				val disp = MsgUtil.buildDispatch(sender, messageName, messageName + "(" + payload + ")", qakName)
				val client = CoapClient(coapAddress)
				client.setTimeout(1L)
				client.put(disp.toString(), MediaTypeRegistry.TEXT_PLAIN)
				client.shutdown()
			}
		}


	}

	fun stop() {
		coapClient.shutdown()
	}
}