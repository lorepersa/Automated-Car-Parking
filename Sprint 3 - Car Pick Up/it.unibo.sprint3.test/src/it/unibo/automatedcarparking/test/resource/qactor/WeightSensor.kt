package it.unibo.automatedcarparking.test.resource.qactor

import it.unibo.automatedcarparking.test.qakutils.IResource
import com.google.gson.Gson
import it.unibo.automatedcarparking.test.qakutils.CoapQAKObserver
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class Weight(var weight : Int)

object WeightSensor : IResource {
	
	private val address = "localhost"
	private val port = 8060
	private val context = "ctxweightsensor"
	private val name = "weightsensor"
	
	private val observer = CoapQAKObserver(address, port, context, name, this)
	private var weight = Weight(0)
	
	private val gson = Gson()
	private val mutex = Mutex()
	
	override fun notify(text: String) {
		runBlocking {
			mutex.withLock {
				weight.weight = gson.fromJson(text, Weight::class.java).weight
			}
		}
	}
	
	fun getWeight() : Int {
		var w = 0
		runBlocking {
			mutex.withLock {
				w = weight.weight
			}
		}
		
		return w
	}
}