package itunibo.qakobserver.internal

import kotlinx.coroutines.sync.Mutex
import it.unibo.kactor.sysUtil
import it.unibo.kactor.QakContext
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.sync.withLock
import org.eclipse.californium.core.CoapClient
import itunibo.qakobserver.IQakResource

internal object QakResourceManager {
	
	private val localResources = mutableSetOf<LocalQakResource>()
	private val remoteResources = mutableSetOf<ProxyQakResource>()
	private val mutex = Mutex()
	
	suspend fun getQakResourceForObservable(observable : ActorBasic) : IQakResource {
		lateinit var result : IQakResource
		mutex.withLock {
			val resource = LocalQakResource(observable)
			localResources.add(resource) // if not already added (set)
			result = localResources.find { it == resource }!!
		}
		
		return result
	}
	
	suspend fun getQakResourceForObserver(observer : ActorBasic, observableName : String) : IQakResourceInternal {
		lateinit var result : IQakResourceInternal
		mutex.withLock {
			val observerName = observer.getName()
			val observerContext = sysUtil.solve("qactor($observerName,CTX,_)", "CTX")!!
			val observableContext = sysUtil.solve("qactor($observableName,CTX,_)", "CTX")!!
			
			if (observerContext.equals(observableContext)) {
				// local
				val observable = sysUtil.getActor(observableName)!!
				val resource = LocalQakResource(observable)
				localResources.add(resource) // if not already added (set)
				result = localResources.find { it == resource }!!
			} else {
				// remote
				var observable = remoteResources.find { it.getActorName().equals(observableName) }
				
				if (observable == null) {
					val host = sysUtil.solve("getCtxHost($observableContext,H)", "H")!!
					val port = sysUtil.solve("getCtxPort($observableContext,P)", "P")!!
					val url = "coap://$host:$port/$observableContext/$observableName"
					val coapClient = CoapClient(url)
					observable = ProxyQakResource(observableName, coapClient)
					remoteResources.add(observable)
				}
				
				result = observable
			}

		}
		
		return result
	}
}