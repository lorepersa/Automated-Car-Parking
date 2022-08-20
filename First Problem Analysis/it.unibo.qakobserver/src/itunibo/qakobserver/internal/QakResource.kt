package itunibo.qakobserver.internal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import itunibo.qakobserver.IQakResource
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

internal abstract class QakResource : IQakResource, IQakResourceInternal {

	private val localObservers = mutableSetOf<IQakObserverInternal>()
	private var latestResource: String? = null
	private val observersMutex = Mutex()
	private val resourceMutex = Mutex()
	private val registrationMutex = Mutex()

	suspend protected fun updateLocalObservers(input: String) = withContext(Dispatchers.Default) {
		resourceMutex.withLock {
			latestResource = input
		}
		observersMutex.withLock {
			localObservers.forEach {
				it.update(input)
			}
		}
	}

	protected abstract suspend fun onRegister()
	protected abstract suspend fun onUnregister(): Boolean
	abstract fun getActorName(): String

	suspend final override fun registerLocalObserver(observer: IQakObserverInternal) =
		withContext<Unit>(Dispatchers.Default) {
			var currentResource: String? = null
			var ok = false
			observersMutex.withLock {
				ok = localObservers.add(observer)
			}

			if (ok) { // only if not previously registered
				registrationMutex.withLock {
					onRegister()
				}
				resourceMutex.withLock {
					currentResource = latestResource
				}
			}

			currentResource?.let {
				observer.update(it)
			}
		}

	suspend final override fun unregisterLocalObserver(observer: IQakObserverInternal) =
		withContext<Unit>(Dispatchers.Default) {
			var ok = false
			observersMutex.withLock {
				ok = localObservers.remove(observer)
			}
			if (ok) {
				var deleteResource = false
				registrationMutex.withLock {
					deleteResource = onUnregister()
				}
				if (deleteResource) {
					latestResource = null
				}
			}

		}

}