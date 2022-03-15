package itunibo.qakobserver.internal

internal interface IQakResourceInternal {
	
	
	suspend fun registerLocalObserver(observer : IQakObserverInternal)
	
	suspend fun unregisterLocalObserver(observer : IQakObserverInternal)
}