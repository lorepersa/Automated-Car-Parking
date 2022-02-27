/* Generated by AN DISI Unibo */ 
package it.unibo.logicalweightsensor

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Logicalweightsensor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var CAR_WEIGHT_THRESHOLD = 750.0f
				var th_reached = false
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[logicalweightsensor] | [State] init | Entry point.")
						
									CAR_WEIGHT_THRESHOLD = 750.0f
									th_reached = false
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[logicalweightsensor] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						updateResourceRep( "indoor_area(FREE)"  
						)
						println("[logicalweightsensor] | [State] init | CAR_WEIGHT_THRESHOLD: $CAR_WEIGHT_THRESHOLD.")
						println("[logicalweightsensor] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="handleWeightEvent", cond=doswitch() )
				}	 
				state("handleWeightEvent") { //this:State
					action { //it:State
						println("[logicalweightsensor] | [State] handleWeightEvent | Entry point.")
						if( checkMsgContent( Term.createTerm("weight(W)"), Term.createTerm("weight(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var W = payloadArg(0).toFloat()  
								println("[logicalweightsensor] | [State] handleWeightEvent | Received event weight: $W.")
								if(  th_reached  
								 ){if(  W <= CAR_WEIGHT_THRESHOLD  
								 ){println("[logicalweightsensor] | [State] handleWeightEvent | Emit indoor_area_free.")
								 th_reached = false  
								emit("indoor_area_free", "indoor_area_free(X)" ) 
								updateResourceRep( "indoor_area(FREE)"  
								)
								}
								}
								else
								 {if(  W > CAR_WEIGHT_THRESHOLD  
								  ){println("[logicalweightsensor] | [State] handleWeightEvent | Emit indoor_area_occupied.")
								  th_reached = true  
								 emit("indoor_area_occupied", "indoor_area_occupied(X)" ) 
								 updateResourceRep( "indoor_area(OCCUPIED)"  
								 )
								 }
								 }
						}
						println("[logicalweightsensor] | [State] handleWeightEvent | Exit point.")
					}
					 transition(edgeName="t_weight_filter2",targetState="handleWeightEvent",cond=whenEvent("weight"))
					transition(edgeName="t_weight_filter3",targetState="init",cond=whenRequest("setup"))
				}	 
			}
		}
}
