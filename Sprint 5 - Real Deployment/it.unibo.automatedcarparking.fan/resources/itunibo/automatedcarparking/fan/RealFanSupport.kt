package itunibo.automatedcarparking.fan

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.Channel
import java.lang.Process
import java.lang.ProcessBuilder
import kotlin.concurrent.thread

object RealFanSupport {

	val input_channel = Channel<Boolean>()
	val output_channel = Channel<Boolean>()

	fun initialize() {
	    thread(start = true, isDaemon = true) {
	        var pb = ProcessBuilder("/usr/bin/python3", "fan.py", "--setup")
	        var fan = pb.start()
	        fan.waitFor()
	        pb = ProcessBuilder("/usr/bin/python3", "fan.py", "--stop")
	        fan = pb.start()
	        fan.waitFor()
	        runBlocking {
	            for (msg in input_channel) {
	                if (msg) {
	                    pb = ProcessBuilder("/usr/bin/python3", "fan.py", "--start")
	                    fan = pb.start()
	                    fan.waitFor()
	                    output_channel.send(true)
	                } else {
	                    pb = ProcessBuilder("/usr/bin/python3", "fan.py", "--stop")
	                    fan = pb.start()
	                    fan.waitFor()
	                    output_channel.send(true)
	                } 
	            }
	        }

            }
    	}
	
	suspend fun turnOn(actor : ActorBasic) {
	        input_channel.send(true)
	        output_channel.receive()
	        actor.autoMsg("auto_fan_done", "auto_fan_done(X)")
	}
	
	suspend fun turnOff(actor : ActorBasic) {
	        input_channel.send(false)
	        output_channel.receive()
	        actor.autoMsg("auto_fan_done", "auto_fan_done(X)")
	}
}
