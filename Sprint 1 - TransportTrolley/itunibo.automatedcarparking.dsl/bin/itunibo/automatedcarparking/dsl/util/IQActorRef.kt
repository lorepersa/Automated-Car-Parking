package itunibo.automatedcarparking.dsl.util

interface IQActorRef {
	
	fun request(sender : String, msgId : String, msgPayload : String)
	
	fun reply(sender : String, reqId : String, msgId : String, msgPayload : String)
	
	fun forward(sender : String, msgId : String, msgPayload : String)
	
	fun emit(sender : String, msgId : String, msgPayload : String)
	
	fun receive() : Message
}