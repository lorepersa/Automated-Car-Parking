package itunibo.qakobserver.internal

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import itunibo.qakobserver.IQakObserver
import itunibo.qakobserver.IMessageBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

final internal class QakObserver(
	private val owner: ActorBasic,
	private val resource: IQakResourceInternal,
	private val messageBuilder: IMessageBuilder
) : IQakObserver, IQakObserverInternal {

	private val mutex = Mutex()
	private var onObserve = false
	private var latestInput: String? = null

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	final suspend override fun update(input: String) = withContext(Dispatchers.Default) {
		var doUpdate = false
		mutex.withLock {
			if (onObserve) {
				if (latestInput == null || !input.equals(latestInput)) {
					latestInput = input
					doUpdate = true
				}
			}
		}
		if (doUpdate) {
			val message = messageBuilder.buildMessage(input, owner.getName())
			message?.let { // if not null
				owner.scope.launch {
					owner.autoMsg(it)
				}
			}
		}
	}

	final override fun isObserving(): Boolean {
		var result = false
		runBlocking {
			mutex.withLock {
				result = onObserve
			}
		}

		return result
	}

	/**
	 * Start to observe another QActor.
	 * Try to be more efficient if the observable and the observer are QActors of the same context,
	 * otherwise use CoAP.
	 */
	final suspend override fun observe() {
		mutex.withLock {
			onObserve = true
			latestInput = null
		}
		resource.registerLocalObserver(this)
	}

	/**
	 * Cancel the observer relation.
	 */
	final suspend override fun cancel() {
		mutex.withLock {
			onObserve = false
			latestInput = null
		}
		resource.unregisterLocalObserver(this)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as QakObserver

		if (!owner.getName().equals(other.owner.getName(), true)) return false

		return true
	}

	override fun hashCode(): Int {
		return owner.getName().hashCode()
	}
}
