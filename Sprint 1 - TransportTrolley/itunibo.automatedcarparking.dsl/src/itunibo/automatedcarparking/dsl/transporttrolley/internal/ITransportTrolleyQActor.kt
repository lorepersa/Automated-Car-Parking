package itunibo.automatedcarparking.dsl.transporttrolley.internal

interface ITransportTrolleyQActor {
	suspend fun newJob()
	
	suspend fun goTo(destination : String)
	
	suspend fun takeOverCar()
	
	suspend fun releaseCar()
	
	suspend fun start()
	
	suspend fun stop()
	
	suspend fun jobDone()
	
	fun connect(host : String, port : Int)
}