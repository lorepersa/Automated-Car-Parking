package itunibo.qakobserver

interface IQakObserver {
	
	suspend fun observe()
	
	suspend fun cancel()
}