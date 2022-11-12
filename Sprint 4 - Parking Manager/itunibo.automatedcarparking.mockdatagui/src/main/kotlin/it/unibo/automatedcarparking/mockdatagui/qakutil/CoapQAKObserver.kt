
package it.unibo.automatedcarparking.mockdatagui.qakutil

import org.eclipse.californium.core.CoapClient
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.coap.CoAP

class CoapQAKObserver(val address: String,
                      val port: Int,
                      val context: String,
                      val name: String,
                      val resource : IResource
) : CoapHandler {

	private var coapAddress: String = "coap://$address:$port/$context/$name"
    private var coapClient: CoapClient = CoapClient(coapAddress)

    init {
        coapClient.observeAndWait(this)
    }


    override fun onLoad(response: CoapResponse) {
        if (response.code == CoAP.ResponseCode.NOT_FOUND)
            return

        val payload = response.responseText

        runBlocking {
            resource.notify(payload)
        }
    }

    override fun onError() {
        println("ERROR CoapChannel ")
    }

	fun stop() {
		coapClient.shutdown()
	}
}