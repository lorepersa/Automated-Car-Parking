package it.unibo.qakutils

import org.eclipse.californium.core.CoapResponse

class Message {
	
	private var response : CoapResponse? = null
	private var messageName : String = ""
	private var type : String = ""
	private var sender : String = ""
	private var receiver : String = ""
	private var payload : String = ""
	
	constructor(coapResponse : CoapResponse?) {
		
		coapResponse?.let {
			response = coapResponse
			val fields = coapResponse.responseText.substringAfter("(").substringBefore("(").split(",")
			messageName = fields.get(0)
			type = fields.get(1)
			sender = fields.get(2)
			receiver = fields.get(3)
			payload = coapResponse.responseText.substringAfter("(").substringAfter("(").substringBefore(")")
		}
	}
	
	fun isSuccess() : Boolean {
		if (response == null) return false
		
		return response!!.isSuccess()
	}

	fun getSender() : String {
		return sender
	}
	
	fun getReceiver() : String {
		return receiver
	}
	
	fun getName() : String {
		return messageName
	}
	
	fun getPayload() : String {
		return payload
	}
	
	fun getType() : String {
		return type
	}
}