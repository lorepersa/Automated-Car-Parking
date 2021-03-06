/* Generated by AN DISI Unibo */ 
package it.unibo.weightsensor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Weightsensor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[weightsensor] | [State] init | Entry point.")
						updateResourceRep( "weightsensor(0)"  
						)
						println("[weightsensor] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[weightsensor] | [State] wait | Entry point.")
						println("[weightsensor] | [State] wait | Exit point.")
					}
					 transition(edgeName="t00",targetState="updateWeight",cond=whenDispatch("input_weight"))
				}	 
				state("updateWeight") { //this:State
					action { //it:State
						println("[weightsensor] | [State] updateWeight | Entry point.")
						if( checkMsgContent( Term.createTerm("input_weight(W)"), Term.createTerm("input_weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var W = payloadArg(0).toInt()  
								println("[weightsensor] | [State] updateWeight | Weight: $W kG.")
								updateResourceRep( "weightsensor($W)"  
								)
						}
						println("[weightsensor] | [State] updateWeight | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
