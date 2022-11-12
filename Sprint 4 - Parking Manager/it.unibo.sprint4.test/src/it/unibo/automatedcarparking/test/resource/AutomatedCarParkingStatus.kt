package it.unibo.automatedcarparking.test.resource

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.annotations.Nullable
import org.springframework.messaging.converter.GsonMessageConverter
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type

data class IndoorAreaStatus(var weightsensorOn : Boolean = false,
                            var indoorAreaWeight : Int = 0,
                            var indoorAreaReserved : Boolean = false,
                            var indoorAreaEngaged : Boolean = false,
                            var indoorAreaCarEnterTimeoutAlarm : Boolean = false) {}

data class OutdoorAreaStatus(var outsonarOn : Boolean = false,
                             var outdoorAreaDistance : Int = 0,
                             var outdoorAreaReserved : Boolean = false,
                             var outdoorAreaEngaged : Boolean = false,
                             var outdoorAreaDTFREETimeoutAlarm : Boolean = false) {}

data class TemperatureStatus(var parkingAreaTemperature : Int = 0,
                             var parkingAreaTemperatureHigh : Boolean = false) {}

data class FanStatus(var fanOn : Boolean = false,
                     var fanAutomatic : Boolean = false,
                     var fanFailureReason : String = "") {}

data class Position(var column : Int, var row : Int, var direction : String) {}
data class TransportTrolleyStatus(var transportTrolleyStopped : Boolean = false,
                                  var transportTrolleyIdle : Boolean = true,
                                  var transportTrolleyMoveFailed : Boolean = false,
                                  var transportTrolleyCoordinate : Position = Position(0,0, "downDir")) {}

data class ParkingSlot(val number : Int, var status : Status) {}
data class Status(val status : String) {}
data class ParkingSlotsStatus(var parkingSlotsStatus : MutableSet<ParkingSlot> = mutableSetOf()) {}

fun subscribeToStompTopic(url : String, topic : String, handler : StompSessionHandler) : StompSession {
    val transports : MutableList<Transport> = mutableListOf()
    transports.add(WebSocketTransport(StandardWebSocketClient()))
    //transports.add(RestTemplateXhrTransport())
    val sockJsClient = SockJsClient(transports)
    //sockJsClient.setMessageCodec(Jackson2SockJsMessageCodec())
    val stompClient = WebSocketStompClient(sockJsClient)
    stompClient.setMessageConverter(GsonMessageConverter())
    val stompSession = stompClient.connect(url, handler).get()
    stompSession.subscribe(topic, handler)
    return stompSession
}

class ParkingSlotsStatusHandler(val mutex : Mutex, url : String, topic : String) : StompSessionHandlerAdapter() {
    var status = ParkingSlotsStatus()
    val session : StompSession = subscribeToStompTopic(url, topic, this)

    override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}

    override fun getPayloadType(stompHeaders : StompHeaders) : Type {
        return ParkingSlotsStatus::class.java
    }
    override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
        runBlocking {
            mutex.withLock {
                if (payload is ParkingSlotsStatus) {
                    status = payload
                }
            }
        }
    }
    fun getCurrentStatus() : ParkingSlotsStatus {
        return status
    }
}

class TransportTrolleyStatusHandler(val mutex : Mutex, url : String, topic : String) : StompSessionHandlerAdapter() {
    var status = TransportTrolleyStatus(false, true, false, Position(0, 0, "downDir"))
    val session : StompSession = subscribeToStompTopic(url, topic, this)

    override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}

    override fun getPayloadType(stompHeaders : StompHeaders) : Type {
        return TransportTrolleyStatus::class.java
    }
    override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
        runBlocking {
            mutex.withLock {
                if (payload is TransportTrolleyStatus) {
                    status = payload
                }
            }
        }
    }
    fun getCurrentStatus() : TransportTrolleyStatus {
        return status
    }
}

class FanStatusHandler(val mutex : Mutex, url : String, topic : String) : StompSessionHandlerAdapter() {
    var status = FanStatus()
    val session : StompSession = subscribeToStompTopic(url, topic, this)

    override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}

    override fun getPayloadType(stompHeaders : StompHeaders) : Type {
        return FanStatus::class.java
    }
    override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
        runBlocking {
            mutex.withLock {
                if (payload is FanStatus) {
                    status = payload
                }
            }
        }
    }
    fun getCurrentStatus() : FanStatus {
        return status
    }
}

class TemperatureStatusHandler(val mutex : Mutex, url : String, topic : String) : StompSessionHandlerAdapter() {
    var status = TemperatureStatus()
    val session : StompSession = subscribeToStompTopic(url, topic, this)

    override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}

    override fun getPayloadType(stompHeaders : StompHeaders) : Type {
        return TemperatureStatus::class.java
    }
    override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
        runBlocking {
            mutex.withLock {
                if (payload is TemperatureStatus) {
                    status = payload
                }
            }
        }
    }
    fun getCurrentStatus() : TemperatureStatus {
        return status
    }
}

class OutdoorAreaStatusHandler(val mutex : Mutex, url : String, topic : String) : StompSessionHandlerAdapter() {
    var status = OutdoorAreaStatus()
    val session : StompSession = subscribeToStompTopic(url, topic, this)

    override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}

    override fun getPayloadType(stompHeaders : StompHeaders) : Type {
        return OutdoorAreaStatus::class.java
    }
    override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
        runBlocking {
            mutex.withLock {
                if (payload is OutdoorAreaStatus) {
                    status = payload
                }
            }
        }
    }
    fun getCurrentStatus() : OutdoorAreaStatus {
        return status
    }
}

class IndoorAreaStatusHandler(val mutex : Mutex, url : String, topic : String) : StompSessionHandlerAdapter() {
    var status = IndoorAreaStatus()
    val session : StompSession = subscribeToStompTopic(url, topic, this)

    override fun afterConnected(session : StompSession, connectedHeaders : StompHeaders) {}

    override fun getPayloadType(stompHeaders : StompHeaders) : Type {
        return IndoorAreaStatus::class.java
    }
    override fun handleFrame(headers: StompHeaders, @Nullable payload: Any?) {
        runBlocking {
            mutex.withLock {
                if (payload is IndoorAreaStatus) {
                    status = payload
                }
            }
        }
    }
    fun getCurrentStatus() : IndoorAreaStatus {
        return status
    }
}


