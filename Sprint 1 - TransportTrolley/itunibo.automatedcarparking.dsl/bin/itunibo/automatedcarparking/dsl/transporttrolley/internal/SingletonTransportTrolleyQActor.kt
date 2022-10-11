package itunibo.automatedcarparking.dsl.transporttrolley.internal

import kotlinx.coroutines.sync.Semaphore
import it.unibo.kactor.QakContext
import itunibo.automatedcarparking.dsl.util.TcpQActorRef
import itunibo.automatedcarparking.dsl.transporttrolley.TransportTrolleyError

object SingletonTransportTrolleyQActor : ITransportTrolleyQActor {
	
	val sem = Semaphore(1)
	val transporttrolley = TcpQActorRef("transporttrolley")
	val senderName = "internaltransporttrolley"
	
	
	override suspend fun newJob() {
		sem.acquire()
		transporttrolley.request(senderName, "transport_trolley_new_job", "transport_trolley_new_job(X)")
		transporttrolley.receive()
	}
	
	override suspend fun goTo(destination : String) {
		transporttrolley.request(senderName, "transport_trolley_go_to", "transport_trolley_go_to($destination)")
		val reply = transporttrolley.receive()
		
		if (reply.getName().equals("transport_trolley_error")) {
			throw TransportTrolleyError(reply.getPayload())
		}
	}
	
	override suspend fun takeOverCar() {
		transporttrolley.request(senderName, "transport_trolley_take_over_car", "transport_trolley_take_over_car(X)")
		val reply = transporttrolley.receive()
		
		if (reply.getName().equals("transport_trolley_error")) {
			throw TransportTrolleyError(reply.getPayload())
		}
	}
	
	override suspend fun releaseCar() {
		transporttrolley.request(senderName, "transport_trolley_release_car", "transport_trolley_release_car(X)")
		val reply = transporttrolley.receive()
		
		if (reply.getName().equals("transport_trolley_error")) {
			throw TransportTrolleyError(reply.getPayload())
		}
	}
	
	override suspend fun start() {
		transporttrolley.forward(senderName, "transport_trolley_start", "transport_trolley_start(X)")
	}
	
	override suspend fun stop() {
		transporttrolley.forward(senderName, "transport_trolley_stop", "transport_trolley_stop(X)")
	}
	
	override suspend fun jobDone() {
		transporttrolley.forward(senderName, "transport_trolley_job_done", "transport_trolley_job_done(X)")
		sem.release()
	}
}