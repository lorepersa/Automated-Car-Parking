/* Generated by AN DISI Unibo */ 
package it.unibo.outsonar

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Outsonar ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				data class Distance(var distance: Int) {}
				
				val gson = com.google.gson.Gson()
				val resource = itunibo.qakobserver.FactoryQakResource.create(myself)
				
				lateinit var support : itunibo.automatedcarparking.outsonar.OutSonarSupport
				
				fun getJsonDistance(distance : Int) : String {
					return gson.toJson(Distance(distance))
				}
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[outsonar] | [State] init | Entry point.")
						 resource.notify(getJsonDistance(0))  
						 support = itunibo.automatedcarparking.outsonar.OutSonarSupport(myself)  
						println("[outsonar] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[outsonar] | [State] wait | Entry point.")
						println("[outsonar] | [State] wait | Exit point.")
					}
					 transition(edgeName="t00",targetState="updateDistance",cond=whenDispatch("input_distance"))
				}	 
				state("updateDistance") { //this:State
					action { //it:State
						println("[outsonar] | [State] updateDistance | Entry point.")
						if( checkMsgContent( Term.createTerm("input_distance(D)"), Term.createTerm("input_distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var D = payloadArg(0).toInt()  
								println("[outsonar] | [State] updateDistance | Distance: $D cm.")
								 resource.notify(getJsonDistance(D))  
						}
						println("[outsonar] | [State] updateDistance | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}