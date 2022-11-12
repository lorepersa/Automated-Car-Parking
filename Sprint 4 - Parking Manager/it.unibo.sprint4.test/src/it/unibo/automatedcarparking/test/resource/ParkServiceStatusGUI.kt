package it.unibo.automatedcarparking.test.resource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object ParkServiceStatusGUI {
    private val mutex = Mutex()
    private val hostname = "localhost"
    private val port = 8200
    private val httpUrl = "http://$hostname:$port"
    private val client = HttpClient.newBuilder().build()
    private val stompUrl = "ws://$hostname:$port/ws/"
    private val indoorAreaStatus = IndoorAreaStatusHandler(mutex, stompUrl, "/topic/indoorareastatus")
    private val outdoorAreaStatus = OutdoorAreaStatusHandler(mutex, stompUrl, "/topic/outdoorareastatus")
    private val temperatureStatus = TemperatureStatusHandler(mutex, stompUrl, "/topic/temperaturestatus")
    private val fanStatus = FanStatusHandler(mutex, stompUrl, "/topic/fanstatus")
    private val transportTrolleyStatus = TransportTrolleyStatusHandler(mutex, stompUrl, "/topic/transporttrolleystatus")
    private val parkingSlotsStatus = ParkingSlotsStatusHandler(mutex, stompUrl, "/topic/parkingslotsstatus")

    private fun buildHttpRequest(requestId : String) : HttpRequest.Builder {
        return HttpRequest.newBuilder().uri(URI.create("$httpUrl/$requestId"))
    }

    suspend fun fanOn() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("manager_fan_on")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun fanOff() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("manager_fan_off")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun fanManualMode() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("manager_fan_manual_mode")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun fanAutomaticMode() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("manager_fan_automatic_mode")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun transportTrolleyStart() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("manager_transport_trolley_start")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun transportTrolleyStop() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("manager_transport_trolley_stop")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun weightSensorInfoOn() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("weightsensor_info_on")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun weightSensorInfoOff() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("weightsensor_info_off")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun outSonarInfoOn() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("outsonar_info_on")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    suspend fun outSonarInfoOff() {
        withContext(Dispatchers.IO) {
            val request = buildHttpRequest("outsonar_info_off")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            response.await()
        }
    }

    fun indoorAreaGetStatus() : IndoorAreaStatus {
        lateinit var currentStatus : IndoorAreaStatus
        runBlocking {
            mutex.withLock {
                currentStatus = indoorAreaStatus.getCurrentStatus().copy()
            }
        }
        return currentStatus
    }

    fun outdoorAreaGetStatus() : OutdoorAreaStatus {
        lateinit var currentStatus : OutdoorAreaStatus
        runBlocking {
            mutex.withLock {
                currentStatus = outdoorAreaStatus.getCurrentStatus().copy()
            }
        }
        return currentStatus
    }

    fun temperatureGetStatus() : TemperatureStatus {
        lateinit var currentStatus : TemperatureStatus
        runBlocking {
            mutex.withLock {
                currentStatus = temperatureStatus.getCurrentStatus().copy()
            }
        }
        return currentStatus
    }

    fun fanGetStatus() : FanStatus {
        lateinit var currentStatus : FanStatus
        runBlocking {
            mutex.withLock {
                currentStatus = fanStatus.getCurrentStatus().copy()
            }
        }
        return currentStatus
    }

    fun transportTrolleyGetStatus() : TransportTrolleyStatus {
        lateinit var currentStatus : TransportTrolleyStatus
        runBlocking {
            mutex.withLock {
                currentStatus = transportTrolleyStatus.getCurrentStatus().copy()
            }
        }
        return currentStatus
    }

    fun parkingSlotsGetStatus() : ParkingSlotsStatus {
        lateinit var currentStatus : ParkingSlotsStatus
        runBlocking {
            mutex.withLock {
                currentStatus = parkingSlotsStatus.getCurrentStatus().copy()
            }
        }
        return currentStatus
    }
}