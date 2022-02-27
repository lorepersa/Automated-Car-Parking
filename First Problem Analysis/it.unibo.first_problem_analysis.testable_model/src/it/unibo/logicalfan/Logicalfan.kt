/* Generated by AN DISI Unibo */ 
package it.unibo.logicalfan

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Logicalfan ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
			var is_automatic_mode = true 
			var fan_on = false
			var STATUS_FAN = "off"
			var MODE_FAN = "automatic"
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[logicalfan] | [State] init | Entry point.")
						 
									is_automatic_mode = true 
									fan_on = false
									STATUS_FAN = "off"
									MODE_FAN = "automatic"
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[logicalfan] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						updateResourceRep( "status_fan($STATUS_FAN) - mode_fan($MODE_FAN)"  
						)
						println("[logicalfan] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="switchToAutomaticMode", cond=doswitch() )
				}	 
				state("waitInputAutomaticMode") { //this:State
					action { //it:State
						println("[logicalfan] | [State] waitInputAutomaticMode | Entry point.")
						println("[logicalfan] | [State] waitInputAutomaticMode | Exit point.")
					}
					 transition(edgeName="t_logicalfan_input_automatic13",targetState="switchToManualMode",cond=whenDispatch("manual_mode"))
					transition(edgeName="t_logicalfan_input_automatic14",targetState="waitInputAutomaticMode",cond=whenDispatch("automatic_mode"))
					transition(edgeName="t_logicalfan_input_automatic15",targetState="turnOnFan",cond=whenEvent("temperature_high"))
					transition(edgeName="t_logicalfan_input_automatic16",targetState="turnOffFan",cond=whenEvent("temperature_low"))
					transition(edgeName="t_logicalfan_input_automatic17",targetState="init",cond=whenRequest("setup"))
				}	 
				state("waitInputManualMode") { //this:State
					action { //it:State
						println("[logicalfan] | [State] waitInputManualMode | Entry point.")
						println("[logicalfan] | [State] waitInputManualMode | Exit point.")
					}
					 transition(edgeName="t_logicalfan_input_manual18",targetState="waitInputManualMode",cond=whenDispatch("manual_mode"))
					transition(edgeName="t_logicalfan_input_manual19",targetState="switchToAutomaticMode",cond=whenDispatch("automatic_mode"))
					transition(edgeName="t_logicalfan_input_manual20",targetState="turnOnFan",cond=whenDispatch("logical_fan_on"))
					transition(edgeName="t_logicalfan_input_manual21",targetState="turnOffFan",cond=whenDispatch("logical_fan_off"))
					transition(edgeName="t_logicalfan_input_manual22",targetState="init",cond=whenRequest("setup"))
				}	 
				state("switchToManualMode") { //this:State
					action { //it:State
						println("[logicalfan] | [State] switchToManualMode | Entry point.")
						 is_automatic_mode = false  
						 MODE_FAN = "manual"  
						emit("mode_fan_manual", "mode_fan_manual(X)" ) 
						updateResourceRep( "status_fan($STATUS_FAN) - mode_fan($MODE_FAN)"  
						)
						println("[logicalfan] | [State] switchToManualMode | Exit point.")
					}
					 transition( edgeName="goto",targetState="waitInputManualMode", cond=doswitch() )
				}	 
				state("switchToAutomaticMode") { //this:State
					action { //it:State
						println("[logicalfan] | [State] switchToAutomaticMode | Entry point.")
						 is_automatic_mode = true  
						 MODE_FAN = "automatic"  
						emit("mode_fan_automatic", "mode_fan_automatic(X)" ) 
						updateResourceRep( "status_fan($STATUS_FAN) - mode_fan($MODE_FAN)"  
						)
						request("query_state_logical_temperature", "query_state_logical_temperature(X)" ,"logicalthermometer" )  
						println("[logicalfan] | [State] switchToAutomaticMode | Exit point.")
					}
					 transition(edgeName="t023",targetState="turnOnFan",cond=whenReply("logical_temperature_high"))
					transition(edgeName="t024",targetState="turnOffFan",cond=whenReply("logical_temperature_low"))
				}	 
				state("turnOnFan") { //this:State
					action { //it:State
						println("[logicalfan] | [State] turnOnFan | Entry point.")
						 var received_msg = false  
						if(  is_automatic_mode  
						 ){if( checkMsgContent( Term.createTerm("temperature_high(X)"), Term.createTerm("temperature_high(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 received_msg = true  
						}
						if( checkMsgContent( Term.createTerm("logical_temperature_high(X)"), Term.createTerm("logical_temperature_high(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 received_msg = true  
						}
						}
						else
						 {if( checkMsgContent( Term.createTerm("logical_fan_on(X)"), Term.createTerm("logical_fan_on(X)"), 
						                         currentMsg.msgContent()) ) { //set msgArgList
						 		 received_msg = true  
						 }
						 }
						if(  received_msg  
						 ){println("[logicalfan] | [State] turnOnFan | Turn ON fan.")
						 fan_on = true  
						 STATUS_FAN = "on"  
						emit("state_fan_on", "state_fan_on(X)" ) 
						updateResourceRep( "status_fan($STATUS_FAN) - mode_fan($MODE_FAN)"  
						)
						}
						println("[logicalfan] | [State] turnOnFan | Entry point.")
					}
					 transition( edgeName="goto",targetState="waitInputAutomaticMode", cond=doswitchGuarded({ is_automatic_mode  
					}) )
					transition( edgeName="goto",targetState="waitInputManualMode", cond=doswitchGuarded({! ( is_automatic_mode  
					) }) )
				}	 
				state("turnOffFan") { //this:State
					action { //it:State
						println("[logicalfan] | [State] turnOffFan | Entry point.")
						 var received_msg = false  
						if(  is_automatic_mode  
						 ){if( checkMsgContent( Term.createTerm("temperature_low(X)"), Term.createTerm("temperature_low(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 received_msg = true  
						}
						if( checkMsgContent( Term.createTerm("logical_temperature_low(X)"), Term.createTerm("logical_temperature_low(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 received_msg = true  
						}
						}
						else
						 {if( checkMsgContent( Term.createTerm("logical_fan_off(X)"), Term.createTerm("logical_fan_off(X)"), 
						                         currentMsg.msgContent()) ) { //set msgArgList
						 		 received_msg = true  
						 }
						 }
						if(  received_msg  
						 ){println("[logicalfan] | [State] turnOffFan | Turn OFF fan.")
						 fan_on = false  
						 STATUS_FAN = "off"  
						emit("state_fan_off", "state_fan_off(X)" ) 
						updateResourceRep( "status_fan($STATUS_FAN) - mode_fan($MODE_FAN)"  
						)
						}
						println("[logicalfan] | [State] turnOffFan | Exit point.")
					}
					 transition( edgeName="goto",targetState="waitInputAutomaticMode", cond=doswitchGuarded({ is_automatic_mode  
					}) )
					transition( edgeName="goto",targetState="waitInputManualMode", cond=doswitchGuarded({! ( is_automatic_mode  
					) }) )
				}	 
			}
		}
}
