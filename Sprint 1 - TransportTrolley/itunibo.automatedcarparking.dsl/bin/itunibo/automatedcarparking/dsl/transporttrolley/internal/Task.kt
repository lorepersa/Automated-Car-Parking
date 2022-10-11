package itunibo.automatedcarparking.dsl.transporttrolley.internal

import itunibo.automatedcarparking.dsl.transporttrolley.ITask
import itunibo.automatedcarparking.dsl.transporttrolley.CarOperation
import itunibo.automatedcarparking.dsl.transporttrolley.Go
import itunibo.automatedcarparking.dsl.transporttrolley.Send
import itunibo.automatedcarparking.dsl.transporttrolley.Resumable

abstract class ATask : ITask {
	
	// keywords
	override val takeOverCar = CarOperation.takeOverCar
	override val releaseCar = CarOperation.releaseCar
	override val go = Go.go
	override val send = Send.send
	override val start = Resumable.start
	override val stop = Resumable.stop
	
	abstract val transportTrolleyActor : ITransportTrolleyQActor
		
	override suspend infix fun CarOperation.at(destination : String) {
		when (this) {
			CarOperation.takeOverCar -> {
				transportTrolleyActor.goTo(destination)
				transportTrolleyActor.takeOverCar()
			}
			CarOperation.releaseCar -> {
				transportTrolleyActor.goTo(destination)
				transportTrolleyActor.releaseCar()
			}
		}
	}
	
	override suspend infix fun Go.to(destination : String) {
		when (this) {
			Go.go -> transportTrolleyActor.goTo(destination)
		}
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
	
	suspend infix fun Send.command(rhs : CarOperation) {
		when (this) {
			Send.send -> {
				when (rhs) {
					CarOperation.takeOverCar -> transportTrolleyActor.takeOverCar()
					CarOperation.releaseCar -> transportTrolleyActor.releaseCar()
				}
			}
		}
	}
}

object SingletonTask : ATask() {
	override val transportTrolleyActor = SingletonTransportTrolleyQActor
}