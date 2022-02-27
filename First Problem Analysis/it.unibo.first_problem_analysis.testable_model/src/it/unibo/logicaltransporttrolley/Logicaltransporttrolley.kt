/* Generated by AN DISI Unibo */ 
package it.unibo.logicaltransporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Logicaltransporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var SLOTNUM = 0
				var AT_HOME = false
				var CURRENT_STATE = "IDLE"
				var CURRENT_POSITION = "HOME"
				var CURRENT_JOB = "NONE"
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] init | Entry point.")
						 
									SLOTNUM = 0
									AT_HOME = true
									CURRENT_STATE = "IDLE"
									CURRENT_POSITION = "home"
									CURRENT_JOB = "NONE"
						println("[logicaltransporttrolley] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("discardAll") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[logicaltransporttrolley] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						stateTimer = TimerActor("timer_discardAll", 
							scope, context!!, "local_tout_logicaltransporttrolley_discardAll", 1000.toLong() )
					}
					 transition(edgeName="t025",targetState="init",cond=whenTimeout("local_tout_logicaltransporttrolley_discardAll"))   
					transition(edgeName="t026",targetState="discardAll",cond=whenDispatch("transport_trolley_car_park"))
					transition(edgeName="t027",targetState="discardAll",cond=whenDispatch("transport_trolley_car_pickup"))
					transition(edgeName="t028",targetState="discardAll",cond=whenDispatch("transport_trolley_go_home"))
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] wait | Entry point.")
						 CURRENT_JOB = "NONE"  
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] wait | Exit point.")
					}
					 transition(edgeName="t029",targetState="carParkTask",cond=whenDispatch("transport_trolley_car_park"))
					transition(edgeName="t030",targetState="carPickupTask",cond=whenDispatch("transport_trolley_car_pickup"))
					transition(edgeName="t031",targetState="backToHomeTask",cond=whenDispatch("transport_trolley_go_home"))
					transition(edgeName="t032",targetState="discardAll",cond=whenRequest("setup"))
				}	 
				state("wait_or_go_home") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] wait_or_go_home | Entry point.")
						 CURRENT_JOB = "NONE"  
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] wait_or_go_home | Exit point.")
						stateTimer = TimerActor("timer_wait_or_go_home", 
							scope, context!!, "local_tout_logicaltransporttrolley_wait_or_go_home", 3000.toLong() )
					}
					 transition(edgeName="t033",targetState="backToHomeTask",cond=whenTimeout("local_tout_logicaltransporttrolley_wait_or_go_home"))   
					transition(edgeName="t034",targetState="carParkTask",cond=whenDispatch("transport_trolley_car_park"))
					transition(edgeName="t035",targetState="carPickupTask",cond=whenDispatch("transport_trolley_car_pickup"))
					transition(edgeName="t036",targetState="backToHomeTask",cond=whenDispatch("transport_trolley_go_home"))
					transition(edgeName="t037",targetState="discardAll",cond=whenRequest("setup"))
				}	 
				state("backToHomeTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] backToHomeTask | Entry point.")
						if(  !AT_HOME  
						 ){println("[logicaltransporttrolley] | [State] backToHomeTask | Going to HOME")
						 CURRENT_JOB = "BackToHome" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						delay(3000) 
						 AT_HOME = true  
						emit("transport_trolley_position_home", "transport_trolley_position_home(X)" ) 
						println("[logicaltransporttrolley] | [State] backToHomeTask | Reached HOME")
						 CURRENT_STATE = "IDLE"  
						 CURRENT_POSITION = "HOME"  
						emit("transport_trolley_new_state", "transport_trolley_new_state($CURRENT_STATE)" ) 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] backToHomeTask | Exit point.")
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("carParkTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] carParkTask | Entry point.")
						 AT_HOME = false  
						if(  CURRENT_STATE == "IDLE"  
						 ){ CURRENT_STATE = "WORKING"  
						emit("transport_trolley_new_state", "transport_trolley_new_state($CURRENT_STATE)" ) 
						}
						if( checkMsgContent( Term.createTerm("transport_trolley_car_park(SLOTNUM)"), Term.createTerm("transport_trolley_car_park(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 SLOTNUM = payloadArg(0).toInt()  
						}
						 CURRENT_JOB = "CarPark" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] carParkTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="goToIndoorAndTakeOverTask", cond=doswitch() )
				}	 
				state("goToIndoorAndTakeOverTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] goToIndoorAndTakeOverTask | Entry point.")
						println("[logicaltransporttrolley] | [State] goToIndoorAndTakeOverTask | Going to INDOOR")
						delay(3000) 
						println("[logicaltransporttrolley] | [State] goToIndoorAndTakeOverTask | Reached INDOOR")
						emit("transport_trolley_position_indoor", "transport_trolley_position_indoor(X)" ) 
						 CURRENT_POSITION = "INDOOR" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] goToIndoorAndTakeOverTask | Take over car...")
						println("[logicaltransporttrolley] | [State] goToIndoorAndTakeOverTask | Car taked over.")
						println("[logicaltransporttrolley] | [State] goToIndoorAndTakeOverTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="goToSlotNumAndReleaseTask", cond=doswitch() )
				}	 
				state("goToSlotNumAndReleaseTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] goToSlotNumAndReleaseTask | Entry point.")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndReleaseTask | Going to SLOTNUM $SLOTNUM")
						delay(3000) 
						emit("transport_trolley_position_slotnum", "transport_trolley_position_slotnum($SLOTNUM)" ) 
						 CURRENT_POSITION = "$SLOTNUM" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] goToSlotNumAndReleaseTask | Reached SLOTNUM $SLOTNUM")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndReleaseTask | Releasing car...")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndReleaseTask | Car released.")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndReleaseTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait_or_go_home", cond=doswitch() )
				}	 
				state("carPickupTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] carPickupTask | Entry point.")
						 AT_HOME = false  
						if(  CURRENT_STATE == "IDLE"  
						 ){ CURRENT_STATE = "WORKING"  
						emit("transport_trolley_new_state", "transport_trolley_new_state($CURRENT_STATE)" ) 
						}
						if( checkMsgContent( Term.createTerm("transport_trolley_car_pickup(SLOTNUM)"), Term.createTerm("transport_trolley_car_pickup(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 SLOTNUM = payloadArg(0).toInt()  
						}
						 CURRENT_JOB = "CarPickup" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] carPickupTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="goToSlotNumAndTakeOverTask", cond=doswitch() )
				}	 
				state("goToSlotNumAndTakeOverTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] goToSlotNumAndTakeOverTask | Entry point.")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndTakeOverTask | Going to SLOTNUM $SLOTNUM.")
						delay(3000) 
						emit("transport_trolley_position_slotnum", "transport_trolley_position_slotnum($SLOTNUM)" ) 
						 CURRENT_POSITION = "$SLOTNUM" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] goToSlotNumAndTakeOverTask | Reached SLOTNUM $SLOTNUM.")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndTakeOverTask | Take over car...")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndTakeOverTask | Car taked over.")
						println("[logicaltransporttrolley] | [State] goToSlotNumAndTakeOverTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="goToOutdoorAndReleaseTask", cond=doswitch() )
				}	 
				state("goToOutdoorAndReleaseTask") { //this:State
					action { //it:State
						println("[logicaltransporttrolley] | [State] goToOutdoorAndReleaseTask | Entry point.")
						println("[logicaltransporttrolley] | [State] goToOutdoorAndReleaseTask | Going to OUTDOOR.")
						delay(3000) 
						emit("transport_trolley_position_outdoor", "transport_trolley_position_outdoor(X)" ) 
						 CURRENT_POSITION = "OUTDOOR" 
						updateResourceRep( "position($CURRENT_POSITION) - status($CURRENT_STATE) - job($CURRENT_JOB)"  
						)
						println("[logicaltransporttrolley] | [State] goToOutdoorAndReleaseTask | Reached OUTDOOR.")
						println("[logicaltransporttrolley] | [State] goToOutdoorAndReleaseTask | Releasing car...")
						println("[logicaltransporttrolley] | [State] goToOutdoorAndReleaseTask | Car released.")
						println("[logicaltransporttrolley] | [State] goToOutdoorAndReleaseTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait_or_go_home", cond=doswitch() )
				}	 
			}
		}
}
