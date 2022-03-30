package itunibo.qakutils.watchdog

import kotlinx.coroutines.sync.Mutex
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import java.util.Timer
import java.util.TimerTask
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope

class Watchdog(private val owner : ActorBasic, private val timeLimitInMilliseconds : Long, private val messageBuilder : IExpiredTimeAutoMessage = DefaultMessageBuilder) {
	
	
	class AutoMsgTimer(private val owner : ActorBasic, private val time : Long, private val messageBuilder : IExpiredTimeAutoMessage) {
		private val mutex = Mutex()
		private var running = true
		
		suspend fun start() {
			GlobalScope.launch {
				delay(time)
				mutex.withLock {
					if (running) {
						owner.autoMsg(messageBuilder.buildMessage(owner.getName()))
					}
				}
			}

		}
		
		suspend fun stop() {
			mutex.withLock {
				running = false
			}
		}
	}
	
	private val mutex = Mutex()
	private var running = false
	private var timer : AutoMsgTimer? = null
	
	
	suspend fun start() {
		mutex.withLock {
			timer = AutoMsgTimer(owner, timeLimitInMilliseconds, messageBuilder)
			timer!!.start()
		}
	}
	
	suspend fun stop() {
		mutex.withLock {
			if (timer != null) {
				timer!!.stop()
				timer = null
			}
		}
	}
	
}