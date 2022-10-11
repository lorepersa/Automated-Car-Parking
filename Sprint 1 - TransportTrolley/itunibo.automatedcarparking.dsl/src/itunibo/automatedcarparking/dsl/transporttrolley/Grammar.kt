package itunibo.automatedcarparking.dsl.transporttrolley

import itunibo.automatedcarparking.dsl.transporttrolley.internal.SingletonTransportTrolley

fun transportTrolleyConnect(host : String, port : Int) {
	SingletonTransportTrolley.connect(host, port)
}

suspend fun transporttrolley(lambda: suspend ITransportTrolley.() -> Unit) {
	val transporttrolley = SingletonTransportTrolley
	transporttrolley.lambda()
}

interface ITask {
	
	// keywords
	val takeOverCar : CarOperation
	val releaseCar : CarOperation
	val go : Go
	val start : Resumable
	val stop : Resumable
	
	suspend infix fun CarOperation.at(destination : String)
	
	suspend infix fun Go.to(destination : String)
	
	suspend infix fun CarOperation.after(milliseconds : Long)
	
	suspend infix fun Resumable.after(milliseconds : Long)
}

interface ITransportTrolley {
	
	// keywords
	val start : Resumable
	val stop : Resumable
	
	suspend fun task(lambda : suspend ITask.() -> Unit)
	
	suspend infix fun Resumable.after(milliseconds : Long)
	
	// initial configuration
	fun connect(host : String, port : Int)
}