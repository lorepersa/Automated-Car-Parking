package itunibo.qakobserver.internal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import itunibo.qakobserver.IQakResource

internal abstract class QakResource : IQakResource, IQakResourceInternal {
	
	private val localObservers = mutableSetOf<IQakObserverInternal>()
	private lateinit var latestResource : String
	private val mutex = Mutex()
	
	suspend protected fun updateLocalObservers(input : String) {
		mutex.withLock {
			latestResource = input
			localObservers.forEach {
				it.update(input)
			}
		}
	}

	protected abstract fun onRegister()
	protected abstract fun onUnregister()
	abstract fun getActorName() : String
	
	suspend final override fun registerLocalObserver(observer : IQakObserverInternal) {
		mutex.withLock {
			val ok = localObservers.add(observer)
			
			if (ok) { // only if not previously registered
				onRegister()
				if (this::latestResource.isInitialized) { // only if at least one notify was made before
					observer.update(latestResource)
				}
			}
		}
	}
	
	suspend final override fun unregisterLocalObserver(observer : IQakObserverInternal) {
		mutex.withLock {
			val ok = localObservers.remove(observer)
			
			if (ok) {
				onUnregister()
			}
		}
	}
	
}