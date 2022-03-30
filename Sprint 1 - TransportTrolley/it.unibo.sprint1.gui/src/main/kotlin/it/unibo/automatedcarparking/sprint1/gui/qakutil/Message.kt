package it.unibo.automatedcarparking.sprint1.gui.qakutil

class Message(response: String) {

	private var messageName : String = ""
	private var type : String = ""
	private var sender : String = ""
	private var receiver : String = ""
	private var payload : String = ""

	init {
		val fields = response.substringAfter("(").substringBefore("(").split(",")
		messageName = fields.get(0)
		type = fields.get(1)
		sender = fields.get(2)
		receiver = fields.get(3)
		payload = response.substringAfter("(").substringAfter("(").substringBefore(")")
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