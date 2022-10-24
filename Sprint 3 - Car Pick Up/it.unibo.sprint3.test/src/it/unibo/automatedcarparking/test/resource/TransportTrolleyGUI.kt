package it.unibo.automatedcarparking.test.resource

import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import java.lang.reflect.Type
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.URI
import java.net.http.HttpResponse
import com.google.gson.Gson
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import org.springframework.web.socket.sockjs.client.Transport
import org.jetbrains.annotations.Nullable
import org.springframework.messaging.converter.GsonMessageConverter

data class Position(var column : Int,
                    var row : Int,
                    var direction : String)

data class TransportTrolleyStatus(var stopped : Boolean,
                                  var idle : Boolean,
                                  var moveFailed : Boolean,
                                  var coordinate : Position
)

object TransportTrolleyGUI  : StompSessionHandlerAdapter() {
	val hostname = "localhost"
	val port = 8081
	val httpUrl = "http://$hostname:$port"
	val topic = "/topic/transporttrolleystatus"
	val client = HttpClient.newBuilder().build()
	val gson = Gson()
	var status = TransportTrolleyStatus(false, true, false, Position(0,0,"downDir"))
	
	private val mutex = Mutex()
	
	private val stompUrl = "ws://$hostname:$port/ws/"
	
	init {
		val transports : MutableList<Transport> = mutableListOf()
		transports.add(WebSocketTransport(StandardWebSocketClient()))
		//transports.add(RestTemplateXhrTransport())
		val sockJsClient = SockJsClient(transports)
		//sockJsClient.setMessageCodec(Jackson2SockJsMessageCodec())
		val stompClient = WebSocketStompClient(sockJsClient)
		stompClient.setMessageConverter(GsonMessageConverter())
		val stompSession = stompClient.connect(stompUrl, this).get()
		
		stompSession.subscribe(topic, this)
	}
	
	override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}
		
	override fun getPayloadType(stompHeaders : StompHeaders) : Type {
		return TransportTrolleyStatus::class.java
	}
			
	override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
		runBlocking {
			mutex.withLock {
				if (payload is TransportTrolleyStatus) {
					status.stopped = payload.stopped
					status.idle = payload.idle
					status.moveFailed = payload.moveFailed
					status.coordinate = payload.coordinate
				}
			}
		}
	}
	
	private fun buildHttpRequest(requestId : String) : HttpRequest.Builder {
		return HttpRequest.newBuilder().uri(URI.create("$httpUrl/$requestId"))
	}
	
	suspend fun transportTrolleyStart() {
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("transport_trolley_start")
				.POST(HttpRequest.BodyPublishers.ofString(""))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			response.await()
		}
	}
	
	suspend fun transportTrolleyStop() {
		withContext(Dispatchers.IO) {
			val request = buildHttpRequest("transport_trolley_stop")
				.POST(HttpRequest.BodyPublishers.ofString(""))
				.build()
			val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			response.await()
		}
	}
	
	fun transportTrolleyGetStatus() : TransportTrolleyStatus {
		lateinit var currentStatus : TransportTrolleyStatus
		runBlocking {
			mutex.withLock {
				currentStatus = status.copy()
			}
		}
		return currentStatus
	}

}