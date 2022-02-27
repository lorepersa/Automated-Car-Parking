/* Generated by AN DISI Unibo */ 
package it.unibo.physicalthermometer

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Physicalthermometer ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[physicalthermometer] | [State] init | Entry point.")
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[physicalthermometer] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						updateResourceRep( "temperature(18.0)"  
						)
						println("[physicalthermometer] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[physicalthermometer] | [State] wait | Entry point.")
						if( checkMsgContent( Term.createTerm("input_temperature(T)"), Term.createTerm("input_temperature(T)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val T = payloadArg(0).toFloat()  
								emit("temperature", "temperature($T)" ) 
								updateResourceRep( "temperature($T)"  
								)
						}
						println("[physicalthermometer] | [State] wait | Exit point.")
					}
					 transition(edgeName="t08",targetState="wait",cond=whenDispatch("input_temperature"))
					transition(edgeName="t09",targetState="init",cond=whenRequest("setup"))
				}	 
			}
		}
}