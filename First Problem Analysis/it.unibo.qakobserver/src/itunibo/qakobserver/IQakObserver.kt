package itunibo.qakobserver

interface IQakObserver {
	
	fun isObserving() : Boolean
	
	suspend fun observe()
	
	suspend fun cancel()
}