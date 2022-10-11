package itunibo.qakutils.watchdog

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

class Watchdog(private val owner : ActorBasic, private val timeLimitInMilliseconds : Long, private val messageBuilder : IExpiredTimeAutoMessage = DefaultMessageBuilder) {
	
	private var running = false
	private var timer : Job? = null
	
	suspend fun start() {
		stop()
		timer = owner.scope.launch {
			delay(timeLimitInMilliseconds)
			owner.autoMsg(messageBuilder.buildMessage(owner.getName()))
		}
		running = true
	}
	
	suspend fun stop() {
		running = false
		timer?.let {
			it.cancel()
			it.join()
		}
		timer = null
	}
	
	fun isRunning() : Boolean {
		return running
	}
	
}