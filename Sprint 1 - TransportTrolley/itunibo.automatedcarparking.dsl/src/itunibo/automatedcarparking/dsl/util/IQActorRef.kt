package itunibo.automatedcarparking.dsl.util

interface IQActorRef {
	
	suspend fun request(sender : String, msgId : String, msgPayload : String)
	
	suspend fun reply(sender : String, reqId : String, msgId : String, msgPayload : String)
	
	suspend fun forward(sender : String, msgId : String, msgPayload : String)
	
	suspend fun emit(sender : String, msgId : String, msgPayload : String)
	
	suspend fun receive() : Message
}