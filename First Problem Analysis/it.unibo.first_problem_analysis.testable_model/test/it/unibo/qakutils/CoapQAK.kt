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


class CoapResource(var coapQak : CoapQAK) : CoapHandler {

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
	
	private var coapAddress : String
	private var qakName : String
	private var coapClient : CoapClient
	private var channel = Channel<String>()
	private var coapResource = CoapResource(this)
	private var resource : String = ""
	
	constructor(address : String, port : Int, context : String, name : String) {
		coapAddress = "coap://$address:$port/$context/$name"
		qakName = name
		coapClient = CoapClient(coapAddress)
		coapClient.observe(coapResource)
	}
	
	@Synchronized
	fun updateResource(newResource : String) {
		resource = newResource
	}
	
	@Synchronized
	fun readResource() : String {
		return resource
	}
	
	fun setup() {
		updateResource("")
		syncRequest("test_setup", "setup", "X")
	}
	
	fun getName() : String {
		return qakName
	}
	
	fun timedRequest(sender : String, messageName : String, payload : String, timeLimitMilliseconds : Long) : Message {
		val req = MsgUtil.buildRequest(sender, messageName, "$messageName($payload)", qakName)
		coapClient.timeout = timeLimitMilliseconds
		var reply = coapClient.put(req.toString(), MediaTypeRegistry.TEXT_PLAIN)
		
		if (reply != null && !reply.isSuccess()) return Message(null)
		
		return Message(reply)
	}
	
	fun syncRequest(sender : String, messageName : String, payload : String) : Message {
		return timedRequest(sender, messageName, payload, 0) // wait until reply (no time limit)
	}
	
	fun sendDispatch(sender : String, messageName : String, payload : String) {
		val disp = MsgUtil.buildDispatch(sender, messageName, messageName + "(" + payload + ")", qakName)
		coapClient.setTimeout(1L)
		coapClient.put(disp.toString(), MediaTypeRegistry.TEXT_PLAIN)
	}
	
	fun stop() {
		coapClient.shutdown()
	}
}

class CoapQAKFactory(address : String, port : Int, context : String) {
	private val address = address
	private val port = port
	private val context = context
	
	fun getCoapQAK(name : String) : CoapQAK {
		return CoapQAK(address, port, context, name)
	}
}