package itunibo.qakobserver

interface IQakResource {
	
	suspend fun notify(input : String)
}