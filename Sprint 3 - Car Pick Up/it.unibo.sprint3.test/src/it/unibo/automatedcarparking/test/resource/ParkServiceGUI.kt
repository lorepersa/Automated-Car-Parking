package it.unibo.automatedcarparking.test.resource

import java.net.http.HttpRequest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpResponse
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers 

class ParkServiceGUI {
	val url = "http://localhost:8100"
	val client = HttpClient.newBuilder().build()
	
	private fun buildHttpRequest(requestId : String) : HttpRequest.Builder {
		return HttpRequest.newBuilder().uri(URI.create("$url/$requestId"))
	}
	
	suspend fun parkingCarInterest() : Int {
		var slotnum : Int = 0
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("parking_car_interest")
				.POST(HttpRequest.BodyPublishers.ofString(""))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			slotnum = response.await().body().toInt()
		}
		
		println("received slotnum $slotnum")
		return slotnum
	}
	
	suspend fun carEnter(slotnum : Int) : String {
		lateinit var tokenid : String
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("car_enter")
				.POST(HttpRequest.BodyPublishers.ofString("$slotnum"))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			tokenid = response.await().body()
		}
		println("received tokenid $tokenid")
		return tokenid
	}

	suspend fun carPickUp(tokenid : String) : Boolean {
		var pick_up_ok : Boolean = false
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("car_pick_up")
				.POST(HttpRequest.BodyPublishers.ofString(tokenid))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			if (response.await().body().equals("ok")) {
				pick_up_ok = true;
			}
		}
		return pick_up_ok
	}
}