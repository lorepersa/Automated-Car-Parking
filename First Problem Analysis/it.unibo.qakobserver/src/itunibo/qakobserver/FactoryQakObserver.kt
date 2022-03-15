package itunibo.qakobserver

import it.unibo.kactor.ActorBasic
import itunibo.qakobserver.internal.QakResourceManager
import itunibo.qakobserver.internal.QakObserver
import kotlinx.coroutines.runBlocking
import itunibo.qakobserver.internal.IQakResourceInternal

object FactoryQakObserver {
	
	fun create(owner : ActorBasic,
					   observableName : String,
					   messageBuilder : IMessageBuilder = DefaultMessageBuilder) : IQakObserver {
		
		lateinit var resource : IQakResourceInternal
		runBlocking {
			resource = QakResourceManager.getQakResourceForObserver(owner, observableName)
		}
		
		val observer = QakObserver(owner, resource, messageBuilder)
		return observer
	}
}