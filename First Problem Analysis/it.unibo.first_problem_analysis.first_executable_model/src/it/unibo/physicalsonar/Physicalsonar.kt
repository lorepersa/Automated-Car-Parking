/* Generated by AN DISI Unibo */ 
package it.unibo.physicalsonar

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Physicalsonar ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[physicalsonar] | [State] init | Entry point.")
						println("[physicalsonar] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[physicalsonar] | [State] wait | Entry point.")
						if( checkMsgContent( Term.createTerm("input_distance(D)"), Term.createTerm("input_distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = payloadArg(0).toFloat()  
								emit("distance", "distance($D)" ) 
						}
						println("[physicalsonar] | [State] wait | Exit point.")
					}
					 transition(edgeName="t02",targetState="wait",cond=whenDispatch("input_distance"))
				}	 
			}
		}
}
