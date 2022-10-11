package itunibo.automatedcarparking.dsl.transporttrolley

import itunibo.automatedcarparking.dsl.transporttrolley.internal.SingletonTransportTrolley

suspend fun transporttrolley(lambda: suspend ITransportTrolley.() -> Unit) {
	val transporttrolley = SingletonTransportTrolley
	transporttrolley.lambda()
}

interface ITask {
	
	// keywords
	val takeOverCar : CarOperation
	val releaseCar : CarOperation
	val go : Go
	val send : Send
	val start : Resumable
	val stop : Resumable
	
	suspend infix fun CarOperation.at(destination : String)
	
	suspend infix fun Go.to(destination : String)
	
	suspend infix fun Send.command(rhs : Resumable)
}

interface ITransportTrolley {
	
	// keywords
	val send : Send
	val start : Resumable
	val stop : Resumable
	
	suspend fun task(lambda : suspend ITask.() -> Unit)
	
	suspend infix fun Send.command(rhs : Resumable)
}