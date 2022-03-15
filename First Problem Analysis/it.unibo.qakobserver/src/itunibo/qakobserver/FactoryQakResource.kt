package itunibo.qakobserver

import it.unibo.kactor.ActorBasic
import itunibo.qakobserver.internal.QakResourceManager
import kotlinx.coroutines.runBlocking

object FactoryQakResource {
	
	fun create(owner : ActorBasic) : IQakResource {
		lateinit var result : IQakResource
		runBlocking {
			result =  QakResourceManager.getQakResourceForObservable(owner)
		}
		return result
	}
}