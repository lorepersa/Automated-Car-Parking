package itunibo.automatedcarparking.dsl.transporttrolley.internal

import itunibo.automatedcarparking.dsl.transporttrolley.ITransportTrolley
import itunibo.automatedcarparking.dsl.transporttrolley.ITask
import itunibo.automatedcarparking.dsl.transporttrolley.Resumable
import kotlinx.coroutines.delay

abstract class ATransportTrolley : ITransportTrolley {

	// keywords
	override val start = Resumable.start
	override val stop = Resumable.stop

	abstract val transportTrolleyActor: ITransportTrolleyQActor
	abstract val taskRef: ITask

	override suspend fun task(lambda: suspend ITask.() -> Unit) {

		transportTrolleyActor.newJob()
		try {
			taskRef.lambda()
		} catch (e: Exception) {
			transportTrolleyActor.jobDone()
			throw e
		}

		transportTrolleyActor.jobDone()

	}

	override suspend infix fun Resumable.after(milliseconds: Long) {

		if (milliseconds > 0) {
			delay(milliseconds)
		}

		when (this) {
			Resumable.start -> transportTrolleyActor.start()
			Resumable.stop -> transportTrolleyActor.stop()
		}
	}

	override fun connect(host: String, port: Int) {
		transportTrolleyActor.connect(host, port)
	}
}

object SingletonTransportTrolley : ATransportTrolley() {
	override val transportTrolleyActor = SingletonTransportTrolleyQActor
	override val taskRef = SingletonTask
}
