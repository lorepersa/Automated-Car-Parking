package itunibo.automatedcarparking.outsonar

import it.unibo.kactor.ActorBasic
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.lang.Process
import java.lang.ProcessBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object RealOutSonarSupport {

        val input_channel = Channel<Boolean>()
        val output_channel = Channel<Int>()

	fun initialize() {
	    thread(start = true, isDaemon = true) {
	        runBlocking {
	             for(msg in input_channel) {
	                 val pb = ProcessBuilder("/usr/bin/python3", "sonar.py")
	                 val sonar = pb.start()
	                 val reader = BufferedReader(InputStreamReader(sonar.getInputStream()))
                         var D : Int = 0
                         withContext(Dispatchers.IO) {
                              D = reader.readLine().toInt()
                         }
                         sonar.waitFor()
                         output_channel.send(D)
	             }
	        }

            }
    	}
	

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @kotlinx.coroutines.ObsoleteCoroutinesApi
        fun register(actor : ActorBasic) {
                actor.scope.launch {
                    var lastD : Int = -1
                    while(true) {
                        input_channel.send(true)
                        val D = output_channel.receive()
                        if (D != lastD) {
                            actor.autoMsg("input_distance", "input_distance($D)")
                            lastD = D
                        }
                        delay(1000)
                    }
                }
        }
}





 
