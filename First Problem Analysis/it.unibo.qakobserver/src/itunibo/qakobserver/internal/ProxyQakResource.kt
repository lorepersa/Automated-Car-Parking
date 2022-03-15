package itunibo.qakobserver.internal

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.CoAP
import kotlinx.coroutines.runBlocking

final internal class ProxyQakResource(val remoteActorName : String, val coapClient : CoapClient) : QakResource(), CoapHandler {
	
	private var registrationCounter = 0
	private lateinit var observeRelation : CoapObserveRelation
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun onLoad(response: CoapResponse) {
		if (response.code == CoAP.ResponseCode.NOT_FOUND)
			return
		
		val text = response.responseText
		runBlocking {
			notify(text)
		}
	}

	override fun onError() {
		println("ERROR on ProxyQakResource... ")
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend override fun notify(input : String) {
		updateLocalObservers(input)
	}
	
	override fun onRegister() {
		if (registrationCounter == 0) {
			observeRelation = coapClient.observeAndWait(this)
		}
		
		registrationCounter++
	}
	
		
	override fun onUnregister() {
		if (registrationCounter > 0) {
			registrationCounter--
			
			if (registrationCounter == 0) {
				observeRelation.proactiveCancel()
			}
		}
		
		registrationCounter++
	}
	
	override fun getActorName() : String {
		return remoteActorName
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as ProxyQakResource

		if (!remoteActorName.equals(other.remoteActorName, true)) return false

		return true
	}

	override fun hashCode(): Int{
		return remoteActorName.hashCode()
	}
	
}