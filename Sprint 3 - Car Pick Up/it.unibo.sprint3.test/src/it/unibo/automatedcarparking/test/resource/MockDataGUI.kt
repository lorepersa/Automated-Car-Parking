package it.unibo.automatedcarparking.test.resource

import java.net.http.HttpRequest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpResponse
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

object MockDataGUI {
	val url = "http://localhost:8099"
	val client = HttpClient.newBuilder().build()
	
	private fun buildHttpRequest(requestId : String) : HttpRequest.Builder {
		return HttpRequest.newBuilder().uri(URI.create("$url/$requestId"))
	}
	
	suspend fun setWeight(weight : Int) {
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("set_weight")
				.POST(HttpRequest.BodyPublishers.ofString("$weight"))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			response.await()
		}

	}

	suspend fun setDistance(distance : Int) {
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("set_distance")
				.POST(HttpRequest.BodyPublishers.ofString("$distance"))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			response.await()
		}

	}

	suspend fun resetParkingSlots() {
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("reset_parkingslots")
				.POST(HttpRequest.BodyPublishers.ofString(""))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			response.await()
		}
	}
}