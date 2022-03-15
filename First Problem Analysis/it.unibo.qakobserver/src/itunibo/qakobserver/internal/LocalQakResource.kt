package itunibo.qakobserver.internal

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock

final internal class LocalQakResource(private val owner : ActorBasic) : QakResource() {
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend override fun notify(input : String) {
		owner.updateResourceRep(input) // update remote observers
		updateLocalObservers(input)
	}
	
	override fun onRegister() {
		// do nothing
	}
	
	override fun onUnregister() {
		// do nothing
	}
	
	override fun getActorName() : String {
		return owner.getName()
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as LocalQakResource

		if (!owner.getName().equals(other.owner.getName(), true)) return false

		return true
	}

	override fun hashCode(): Int{
		return owner.getName().hashCode()
	}
}