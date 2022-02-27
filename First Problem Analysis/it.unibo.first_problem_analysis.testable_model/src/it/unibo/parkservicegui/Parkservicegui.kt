/* Generated by AN DISI Unibo */ 
package it.unibo.parkservicegui

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkservicegui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var SLOTNUM = 0 
				var TOKENID = ""
				var response_car_pickup = ""
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] init | Entry point.")
						 
									SLOTNUM = 0 
									TOKENID = ""
									response_car_pickup = ""
						println("[parkservicegui] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("discardAll") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[parkservicegui] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						stateTimer = TimerActor("timer_discardAll", 
							scope, context!!, "local_tout_parkservicegui_discardAll", 1000.toLong() )
					}
					 transition(edgeName="t053",targetState="init",cond=whenTimeout("local_tout_parkservicegui_discardAll"))   
					transition(edgeName="t054",targetState="discardAll",cond=whenRequest("req_client_accept_in"))
					transition(edgeName="t055",targetState="discardAll",cond=whenReply("inform_in"))
					transition(edgeName="t056",targetState="discardAll",cond=whenRequest("req_client_car_enter"))
					transition(edgeName="t057",targetState="discardAll",cond=whenRequest("req_client_accept_out"))
					transition(edgeName="t058",targetState="discardAll",cond=whenReply("response_car_enter"))
					transition(edgeName="t059",targetState="discardAll",cond=whenReply("accept_out_success"))
					transition(edgeName="t060",targetState="discardAll",cond=whenReply("accept_out_failure"))
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] wait | Entry point.")
						println("[parkservicegui] | [State] wait | Exit point.")
					}
					 transition(edgeName="t061",targetState="interestedInParkingCar",cond=whenRequest("req_client_accept_in"))
					transition(edgeName="t062",targetState="enterIndoor",cond=whenReply("inform_in"))
					transition(edgeName="t063",targetState="carEnter",cond=whenRequest("req_client_car_enter"))
					transition(edgeName="t064",targetState="askForPickUpCar",cond=whenRequest("req_client_accept_out"))
					transition(edgeName="t065",targetState="receiveTokenId",cond=whenReply("response_car_enter"))
					transition(edgeName="t066",targetState="pickUpCar",cond=whenReply("accept_out_success"))
					transition(edgeName="t067",targetState="cannotPickUpCar",cond=whenReply("accept_out_failure"))
					transition(edgeName="t068",targetState="discardAll",cond=whenRequest("setup"))
				}	 
				state("interestedInParkingCar") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] InterestedInParkingCar | Entry point.")
						if( checkMsgContent( Term.createTerm("req_client_accept_in(X)"), Term.createTerm("req_client_accept_in(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
						}
						request("parking_car_interest", "parking_car_interest(io)" ,"businesslogic" )  
						println("[parkservicegui] | [State] InterestedInParkingCar | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("enterIndoor") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] enterIndoor | Entry point.")
						if( checkMsgContent( Term.createTerm("inform_in(SLOTNUM)"), Term.createTerm("inform_in(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 SLOTNUM = payloadArg(0).toInt()  
								println("[parkservicegui] | [State] enterIndoor | Client receives SLOTNUM = $SLOTNUM")
								if(  SLOTNUM > 0  
								 ){answer("req_client_accept_in", "rep_client_accept_in", "rep_client_accept_in($SLOTNUM)"   )  
								}
								else
								 {answer("req_client_accept_in", "rep_client_accept_in", "rep_client_accept_in(0)"   )  
								 }
						}
						println("[parkservicegui] | [State] enterIndoor | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("carEnter") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] CarEnter | Entry point.")
						if( checkMsgContent( Term.createTerm("req_client_car_enter(SLOTNUM)"), Term.createTerm("req_client_car_enter(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 SLOTNUM = payloadArg(0).toInt()  
						}
						request("car_enter", "car_enter($SLOTNUM)" ,"businesslogic" )  
						println("[parkservicegui] | [State] CarEnter | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("receiveTokenId") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] ReceiveTokenId | Entry point.")
						if( checkMsgContent( Term.createTerm("response_car_enter(TOKENID)"), Term.createTerm("response_car_enter(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TOKENID = payloadArg(0).toString()  
								println("[parkservicegui] | [State] ReceiveTokenId | Client receives TOKENID = $TOKENID ")
								answer("req_client_car_enter", "rep_client_car_enter", "rep_client_car_enter($TOKENID)"   )  
						}
						println("[parkservicegui] | [State] ReceiveTokenId | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("askForPickUpCar") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] askForPickUpCar | Entry point.")
						if( checkMsgContent( Term.createTerm("req_client_accept_out(TOKENID)"), Term.createTerm("req_client_accept_out(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 TOKENID = payloadArg(0)  
						}
						println("[parkservicegui] | [State] askForPickUpCar | Ok now it's time to pick up my car")
						request("car_pickup", "car_pickup($TOKENID)" ,"businesslogic" )  
						println("[parkservicegui] | [State] askForPickUpCar | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("pickUpCar") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] pickUpCar | Entry point.")
						if( checkMsgContent( Term.createTerm("accept_out_success(X)"), Term.createTerm("accept_out_success(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[parkservicegui] | [State] pickUpCar | Thank you! Now I'm leaving the outdoor area.. bye")
								answer("req_client_accept_out", "rep_client_accept_out_success", "rep_client_accept_out_success(X)"   )  
						}
						println("[parkservicegui] | [State] pickUpCar | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("cannotPickUpCar") { //this:State
					action { //it:State
						println("[parkservicegui] | [State] cannotPickUpCar | Entry point.")
						if( checkMsgContent( Term.createTerm("accept_out_failure(X)"), Term.createTerm("accept_out_failure(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[parkservicegui] | [State] cannotPickUpCar | My token is not valid. I'll be back.")
								answer("req_client_accept_out", "rep_client_accept_out_failure", "rep_client_accept_out_failure(X)"   )  
						}
						println("[parkservicegui] | [State] cannotPickUpCar | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}