package itunibo.qakobserver.internal

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapObserveRelation
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.CoAP
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import itunibo.qakobserver.IQakResource

final internal class IQakResourceCoapHandler(val qakResource: IQakResource) : CoapHandler {

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun onLoad(response: CoapResponse) {
		if (response.code == CoAP.ResponseCode.NOT_FOUND)
			return

		val text = response.responseText
		runBlocking {
			qakResource.notify(text)
		}
	}

	override fun onError() {
		println("ERROR on ProxyQakResource... ")
	}
}

final internal class ProxyQakResource(val remoteActorName: String, val url: String) : QakResource() {

	private var registrationCounter = 0
	private var coapClient: CoapClient = CoapClient(url)
	private var observeRelation: CoapObserveRelation? = null
	private var handler = IQakResourceCoapHandler(this)

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend override fun notify(input: String) {
		updateLocalObservers(input)
	}

	override suspend fun onRegister() {
		if (registrationCounter == 0) {
			withContext(Dispatchers.IO) {
				observeRelation = coapClient.observe(handler)
			}
		}

		registrationCounter++
	}


	override suspend fun onUnregister(): Boolean {
		if (registrationCounter > 0) {
			registrationCounter--

			if (registrationCounter == 0) {
				withContext(Dispatchers.IO) {
					observeRelation!!.proactiveCancel()
				}
				coapClient.shutdown()
				observeRelation = null
				//delay(2000)
				return true
			}

			return false
		}

		return true
	}

	override fun getActorName(): String {
		return remoteActorName
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as ProxyQakResource

		if (!remoteActorName.equals(other.remoteActorName, true)) return false

		return true
	}

	override fun hashCode(): Int {
		return remoteActorName.hashCode()
	}

}