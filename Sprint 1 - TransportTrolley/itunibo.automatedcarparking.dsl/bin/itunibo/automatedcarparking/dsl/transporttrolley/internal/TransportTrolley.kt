package itunibo.automatedcarparking.dsl.transporttrolley.internal

import itunibo.automatedcarparking.dsl.transporttrolley.ITransportTrolley
import itunibo.automatedcarparking.dsl.transporttrolley.ITask
import itunibo.automatedcarparking.dsl.transporttrolley.Send
import itunibo.automatedcarparking.dsl.transporttrolley.Resumable

abstract class ATransportTrolley : ITransportTrolley {
	
	// keywords
	override val send = Send.send
	override val start = Resumable.start
	override val stop = Resumable.stop
	
	abstract val transportTrolleyActor : ITransportTrolleyQActor
	abstract val taskRef : ITask
	
	override suspend fun task(lambda : suspend ITask.() -> Unit) {
		transportTrolleyActor.newJob()
		try {
			taskRef.lambda()
		} catch (e : Exception) {
			transportTrolleyActor.jobDone()
			throw e
		}
		
		transportTrolleyActor.jobDone()
	}
	
	override suspend infix fun Send.command(rhs : Resumable) {
		when (this) {
			Send.send -> {
				when (rhs) {
					Resumable.start -> transportTrolleyActor.start()
					Resumable.stop -> transportTrolleyActor.stop()
				}
			}
		}
	}
}

object SingletonTransportTrolley : ATransportTrolley() {
	override val transportTrolleyActor = SingletonTransportTrolleyQActor
	override val taskRef = SingletonTask
}
