/* Generated by AN DISI Unibo */ 
package it.unibo.ctxthermometer
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "thermometer", this, "systembusinesslogic.pl", "sysRules.pl"
	)
}

