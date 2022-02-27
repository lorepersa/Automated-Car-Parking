/* Generated by AN DISI Unibo */ 
package it.unibo.logicalthermometer

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Logicalthermometer ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var TMAX = 35.0f
				var T = 0.0f
				var over_TMAX = false
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[logicalthermometer] | [State] init | Entry point.")
						
									TMAX = 35.0f
									T = 0.0f
									over_TMAX = false
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[logicalthermometer] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						updateResourceRep( "temperature(LOW)"  
						)
						println("[logicalthermometer] | [State] init | TMAX: $TMAX.")
						println("[logicalthermometer] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="handleTemperatureEvent", cond=doswitch() )
				}	 
				state("handleTemperatureEvent") { //this:State
					action { //it:State
						println("[logicalthermometer] | [State] handleTemperatureEvent | Entry point.")
						if( checkMsgContent( Term.createTerm("temperature(C)"), Term.createTerm("temperature(C)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 T = payloadArg(0).toFloat()  
								println("[logicalthermometer] | [State] handleTemperatureEvent | Received event temperature: $T.")
								if(  over_TMAX  
								 ){if(  T <= TMAX  
								 ){println("[logicalthermometer] | [State] handleTemperatureEvent | Emit temperature_low.")
								 over_TMAX = false  
								emit("temperature_low", "temperature_low(X)" ) 
								updateResourceRep( "temperature(LOW)"  
								)
								}
								}
								else
								 {if(  T > TMAX  
								  ){println("[logicalthermometer] | [State] handleTemperatureEvent | Emit temperature_high.")
								  over_TMAX = true  
								 emit("temperature_high", "temperature_high(X)" ) 
								 updateResourceRep( "temperature(HIGH)"  
								 )
								 }
								 }
						}
						println("[logicalthermometer] | [State] handleTemperatureEvent | Exit point.")
					}
					 transition(edgeName="t_temp_filter10",targetState="handleTemperatureEvent",cond=whenEvent("temperature"))
					transition(edgeName="t_temp_filter11",targetState="init",cond=whenRequest("setup"))
					transition(edgeName="t_temp_filter12",targetState="sendLogicalTemperature",cond=whenRequest("query_state_logical_temperature"))
				}	 
				state("sendLogicalTemperature") { //this:State
					action { //it:State
						println("[logicalthermometer] | [State] sendLogicalTemperature | Entry point.")
						if( checkMsgContent( Term.createTerm("query_state_logical_temperature(X)"), Term.createTerm("query_state_logical_temperature(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  T > TMAX  
								 ){answer("query_state_logical_temperature", "logical_temperature_high", "logical_temperature_high(X)"   )  
								}
								else
								 {answer("query_state_logical_temperature", "logical_temperature_low", "logical_temperature_low(X)"   )  
								 }
						}
						println("[logicalthermometer] | [State] sendLogicalTemperature | Exit point.")
					}
					 transition( edgeName="goto",targetState="handleTemperatureEvent", cond=doswitch() )
				}	 
			}
		}
}
