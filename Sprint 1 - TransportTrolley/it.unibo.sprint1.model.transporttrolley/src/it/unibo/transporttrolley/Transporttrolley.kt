/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var DESTINATION = "HOME"
				var POSITION_NAME = "HOME"
				var stopped = false
				var moveFailed = false
				var atHome = true
				var car_taken_over = false
				var handling_job = false
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] init | Entry point.")
						println("[transporttrolley] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] wait | Entry point.")
						println("[transporttrolley] | [State] wait | Exit point.")
					}
					 transition(edgeName="t00",targetState="handleResumableMessages",cond=whenDispatch("transport_trolley_start"))
					transition(edgeName="t01",targetState="handleResumableMessages",cond=whenDispatch("transport_trolley_stop"))
					transition(edgeName="t02",targetState="handleNewJob",cond=whenRequestGuarded("transport_trolley_new_job",{ !stopped && !handling_job  
					}))
					transition(edgeName="t03",targetState="handleNewDestination",cond=whenRequestGuarded("transport_trolley_go_to",{ !stopped  
					}))
					transition(edgeName="t04",targetState="handleCarTask",cond=whenRequestGuarded("transport_trolley_take_over_car",{ !stopped  
					}))
					transition(edgeName="t05",targetState="handleCarTask",cond=whenRequestGuarded("transport_trolley_release_car",{ !stopped  
					}))
					transition(edgeName="t06",targetState="handleJobDone",cond=whenDispatch("transport_trolley_job_done"))
				}	 
				state("handleResumableMessages") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] handleResumableMessages | Entry point.")
						if( checkMsgContent( Term.createTerm("transport_trolley_start(X)"), Term.createTerm("transport_trolley_start(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[transporttrolley] | [State] handleResumableMessages | Received START!.")
								 stopped = false  
								if(  moveFailed  
								 ){println("[transporttrolley] | [State] handleResumableMessages | Last move is failed, reposition transport trolley at HOME!.")
								 atHome = true  
								if(  handling_job && DESTINATION.equals("HOME", true)  
								 ){answer("transport_trolley_go_to", "transport_trolley_arrived_at", "transport_trolley_arrived_at(HOME)"   )  
								}
								}
						}
						if( checkMsgContent( Term.createTerm("transport_trolley_stop(X)"), Term.createTerm("transport_trolley_stop(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[transporttrolley] | [State] handleResumableMessages | Received STOP!.")
								 stopped = true  
						}
						println("[transporttrolley] | [State] handleResumableMessages | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleNewJob") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] handleNewJob | Entry point.")
						 handling_job = true  
						answer("transport_trolley_new_job", "transport_trolley_job_accepted", "transport_trolley_job_accepted(X)"   )  
						println("[transporttrolley] | [State] handleNewJob | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleNewDestination") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] handleNewDestination | Entry point.")
						if( checkMsgContent( Term.createTerm("transport_trolley_go_to(DESTINATION)"), Term.createTerm("transport_trolley_go_to(DESTINATION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 DESTINATION = payloadArg(0)  
								println("[transporttrolley] | [State] handleNewDestination | Received destination: $DESTINATION.")
								if(  DESTINATION.equals(POSITION_NAME)  
								 ){println("[transporttrolley] | [State] handleNewDestination | I'm already at $DESTINATION...")
								answer("transport_trolley_go_to", "transport_trolley_arrived_at", "transport_trolley_arrived_at($DESTINATION)"   )  
								}
								else
								 {delay(5000) 
								 println("[transporttrolley] | [State] handleNewDestination | Arrived at $DESTINATION!.")
								 answer("transport_trolley_go_to", "transport_trolley_arrived_at", "transport_trolley_arrived_at($DESTINATION)"   )  
								  POSITION_NAME = DESTINATION  
								 if(  POSITION_NAME.equals("HOME")  
								  ){ atHome = true  
								 }
								 else
								  { atHome = false  
								  }
								 }
						}
						println("[transporttrolley] | [State] handleNewDestination | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleJobDone") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] handleJobDone | Entry point.")
						 handling_job = false  
						println("[transporttrolley] | [State] handleJobDone | Exit point.")
						stateTimer = TimerActor("timer_handleJobDone", 
							scope, context!!, "local_tout_transporttrolley_handleJobDone", 3000.toLong() )
					}
					 transition(edgeName="t07",targetState="handleGoHome",cond=whenTimeout("local_tout_transporttrolley_handleJobDone"))   
					transition(edgeName="t08",targetState="handleNewJob",cond=whenRequest("transport_trolley_new_job"))
				}	 
				state("handleGoHome") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] handleGoHome | Entry point.")
						delay(3000) 
						 POSITION_NAME = "HOME"  
						println("[transporttrolley] | [State] handleGoHome | Arrived at HOME!.")
						println("[transporttrolley] | [State] handleGoHome | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleCarTask") { //this:State
					action { //it:State
						println("[transporttrolley] | [State] handleCarTask | Entry point.")
						if( checkMsgContent( Term.createTerm("transport_trolley_take_over_car(X)"), Term.createTerm("transport_trolley_take_over_car(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  !car_taken_over  
								 ){println("[transporttrolley] | [State] handleCarTask | Taking over car...")
								delay(1000) 
								 car_taken_over = true  
								println("[transporttrolley] | [State] handleCarTask | Car taken over!")
								answer("transport_trolley_take_over_car", "transport_trolley_car_taken_over", "transport_trolley_car_taken_over(X)"   )  
								}
								else
								 { val REASON = "CANNOT TAKE OVER MORE THAN ONE CAR" 
								 answer("transport_trolley_take_over_car", "transport_trolley_error", "transport_trolley_error($REASON)"   )  
								 }
						}
						if( checkMsgContent( Term.createTerm("transport_trolley_release_car(X)"), Term.createTerm("transport_trolley_release_car(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  car_taken_over  
								 ){println("[transporttrolley] | [State] handleCarTask | Releasing car...")
								delay(1000) 
								 car_taken_over = false  
								println("[transporttrolley] | [State] handleCarTask | Car released!")
								answer("transport_trolley_release_car", "transport_trolley_car_released", "transport_trolley_car_released(X)"   )  
								}
								else
								 { val REASON = "NO CAR TAKED OVER"  
								 answer("transport_trolley_release_car", "transport_trolley_error", "transport_trolley_error($REASON)"   )  
								 }
						}
						println("[transporttrolley] | [State] handleCarTask | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}