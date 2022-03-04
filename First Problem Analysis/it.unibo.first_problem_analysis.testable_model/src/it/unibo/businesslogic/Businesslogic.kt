/* Generated by AN DISI Unibo */ 
package it.unibo.businesslogic

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Businesslogic ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var  indoor_area_free = true
				var  outdoor_area_free = true
				var parking_slots: java.util.Queue<Int> = java.util.LinkedList<Int>(listOf(1, 2, 3, 4, 5, 6))
				var STATUS_PARKING_AREA = arrayOf<String>("FREE", "FREE", "FREE", "FREE", "FREE", "FREE")
				var token_parking_slots = java.util.LinkedList<String>()
				var SLOTNUM = 0
				var RESERVED_SLOTNUM_INDOOR_AREA = 0
				var SLOTNUM_PICKUP = 0
				var TOKENID = ""
				var tokenid_valid = false
				var car_pickup_request = false
				var car_park_request = false
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("[businesslogic] | [State] init | Entry point.")
						
									indoor_area_free = true
									outdoor_area_free = true
									parking_slots = java.util.LinkedList<Int>(listOf(1, 2, 3, 4, 5, 6))
									STATUS_PARKING_AREA = arrayOf<String>("FREE", "FREE", "FREE", "FREE", "FREE", "FREE")
									token_parking_slots = java.util.LinkedList<String>()
									SLOTNUM = 0
									RESERVED_SLOTNUM_INDOOR_AREA = 0
									SLOTNUM_PICKUP = 0
									TOKENID = ""
									tokenid_valid = false
									car_pickup_request = false
									car_park_request = false
						 
									val P1 =  STATUS_PARKING_AREA[0]
									val P2 =  STATUS_PARKING_AREA[1]
									val P3 =  STATUS_PARKING_AREA[2]
									val P4 =  STATUS_PARKING_AREA[3]
									val P5 =  STATUS_PARKING_AREA[4]
									val P6 =  STATUS_PARKING_AREA[5]
						updateResourceRep( "status_parking_slots($P1,$P2,$P3,$P4,$P5,$P6)"  
						)
						println("[businesslogic] | [State] init | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("discardAll") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("setup(X)"), Term.createTerm("setup(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("[businesslogic] | [State] init | Setup.")
								answer("setup", "done", "done(X)"   )  
						}
						stateTimer = TimerActor("timer_discardAll", 
							scope, context!!, "local_tout_businesslogic_discardAll", 1000.toLong() )
					}
					 transition(edgeName="t038",targetState="init",cond=whenTimeout("local_tout_businesslogic_discardAll"))   
					transition(edgeName="t039",targetState="discardAll",cond=whenRequest("parking_car_interest"))
					transition(edgeName="t040",targetState="discardAll",cond=whenRequest("car_enter"))
					transition(edgeName="t041",targetState="discardAll",cond=whenRequest("car_pickup"))
				}	 
				state("running") { //this:State
					action { //it:State
						println("[businesslogic] | [State] running | Entry point.")
						println("[businesslogic] | [State] running | Exit point.")
					}
					 transition(edgeName="t042",targetState="checkCarIndoorArea",cond=whenRequestGuarded("parking_car_interest",{ indoor_area_free  
					}))
					transition(edgeName="t043",targetState="carEnter",cond=whenRequestGuarded("car_enter",{ !indoor_area_free && !car_park_request  
					}))
					transition(edgeName="t044",targetState="sendTokenId",cond=whenDispatch("transport_trolley_at_indoor"))
					transition(edgeName="t045",targetState="carParkDone",cond=whenReply("transport_trolley_car_park_done"))
					transition(edgeName="t046",targetState="setFreeIndoorArea",cond=whenEvent("indoor_area_free"))
					transition(edgeName="t047",targetState="setOccupiedIndoorArea",cond=whenEvent("indoor_area_occupied"))
					transition(edgeName="t048",targetState="checkTokenId",cond=whenRequestGuarded("car_pickup",{ outdoor_area_free && !car_pickup_request  
					}))
					transition(edgeName="t049",targetState="pickUpCarFinished",cond=whenReply("transport_trolley_car_pickup_done"))
					transition(edgeName="t050",targetState="setFreeOutdoorArea",cond=whenEvent("outdoor_area_free"))
					transition(edgeName="t051",targetState="setOccupiedOutdoorArea",cond=whenEvent("outdoor_area_occupied"))
					transition(edgeName="t052",targetState="discardAll",cond=whenRequest("setup"))
				}	 
				state("sendTransportTrolleyBackHome") { //this:State
					action { //it:State
						println("[businesslogic] | [State] sendTransportTrolleyBackHome | Entry point.")
						println("[businesslogic] | [State] sendTransportTrolleyBackHome | Sending the transport trolley back home because there are no other requests")
						forward("transport_trolley_go_home", "transport_trolley_go_home(ok)" ,"logicaltransporttrolley" ) 
						println("[businesslogic] | [State] sendTransportTrolleyBackHome | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("checkCarIndoorArea") { //this:State
					action { //it:State
						println("[businesslogic] | [State] checkCarIndoorArea | Entry point.")
						println("[businesslogic] | [State] checkCarIndoorArea | Checking if there is a parking slot free and if there is a car in the indoor area...")
						println("[businesslogic] | [State] checkCarIndoorArea | Exit point.")
					}
					 transition( edgeName="goto",targetState="acceptIn", cond=doswitchGuarded({ !parking_slots.isEmpty()  
					}) )
					transition( edgeName="goto",targetState="denyAcceptIn", cond=doswitchGuarded({! ( !parking_slots.isEmpty()  
					) }) )
				}	 
				state("acceptIn") { //this:State
					action { //it:State
						println("[businesslogic] | [State] acceptIn | Entry point.")
						 
									RESERVED_SLOTNUM_INDOOR_AREA = parking_slots.poll()
									indoor_area_free = false
						println("[businesslogic] | [State] acceptIn | Car in the indoor area. I'm sending SLOTNUM.")
						answer("parking_car_interest", "inform_in", "inform_in($RESERVED_SLOTNUM_INDOOR_AREA)"   )  
						println("[businesslogic] | [State] acceptIn | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("denyAcceptIn") { //this:State
					action { //it:State
						println("[businesslogic] | [State] denyAcceptIn | Entry point.")
						println("[businesslogic] | [State] denyAcceptIn | Sending failure to client.. ")
						answer("parking_car_interest", "inform_in", "inform_in(0)"   )  
						println("[businesslogic] | [State] denyAcceptIn | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("carEnter") { //this:State
					action { //it:State
						println("[businesslogic] | [State] carEnter | Entry point.")
						if( checkMsgContent( Term.createTerm("car_enter(SLOTNUM)"), Term.createTerm("car_enter(SLOTNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												val client_SLOTNUM = payloadArg(0).toInt()
												STATUS_PARKING_AREA[RESERVED_SLOTNUM_INDOOR_AREA - 1] = "OCCUPIED"
												car_park_request = true
								println("[businesslogic] | [State] carEnter | Received SLOTNUM=$client_SLOTNUM")
								println("[businesslogic] | [State] carEnter | Sending trolley to Indoor...")
								request("transport_trolley_car_park", "transport_trolley_car_park($RESERVED_SLOTNUM_INDOOR_AREA)" ,"logicaltransporttrolley" )  
						}
						println("[businesslogic] | [State] carEnter | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("sendTokenId") { //this:State
					action { //it:State
						println("[businesslogic] | [State] sendTokenId | Entry point.")
						
									// simplified! need to change in random generation
									TOKENID = "PMSx$RESERVED_SLOTNUM_INDOOR_AREA" 
									token_parking_slots.add(TOKENID)
						println("[businesslogic] | [State] carEnter | Created  TOKENID=$TOKENID")
						answer("car_enter", "response_car_enter", "response_car_enter($TOKENID)"   )  
						println("[businesslogic] | [State] sendTokenId | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("carParkDone") { //this:State
					action { //it:State
						println("[businesslogic] | [State] carParkDone | Entry point.")
						 
									car_park_request = false 
									val P1 =  STATUS_PARKING_AREA[0]
									val P2 =  STATUS_PARKING_AREA[1]
									val P3 =  STATUS_PARKING_AREA[2]
									val P4 =  STATUS_PARKING_AREA[3]
									val P5 =  STATUS_PARKING_AREA[4]
									val P6 =  STATUS_PARKING_AREA[5]
						emit("status_parking_slots", "status_parking_slots($P1,$P2,$P3,$P4,$P5,$P6)" ) 
						updateResourceRep( "status_parking_slots($P1,$P2,$P3,$P4,$P5,$P6)"  
						)
						println("[businesslogic] | [State] carParkDone | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("setFreeIndoorArea") { //this:State
					action { //it:State
						println("[businesslogic] | [State] setFreeIndoorArea | Entry point.")
						println("[businesslogic] | [State] setFreeIndoorArea | The weight sensor has notified that the car has left the parking area.")
						 indoor_area_free = true  
						println("[businesslogic] | [State] setFreeIndoorArea | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("setOccupiedIndoorArea") { //this:State
					action { //it:State
						println("[businesslogic] | [State] setOccupiedIndoorArea | Entry point.")
						println("[businesslogic] | [State] setOccupiedIndoorArea | The weight sensor has notified that the car has entered the indoor area.")
						 indoor_area_free = false  
						println("[businesslogic] | [State] setOccupiedIndoorArea | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("checkTokenId") { //this:State
					action { //it:State
						println("[businesslogic] | [State] checkTokenId | Entry point.")
						println("[businesslogic] | [State] checkTokenId | Checking the TokenID...")
						if( checkMsgContent( Term.createTerm("car_pickup(TOKENID)"), Term.createTerm("car_pickup(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
											tokenid_valid = false
											TOKENID = payloadArg(0).toString() 
											val token_index = token_parking_slots.indexOf(TOKENID)
								if(  token_index >= 0  
								 ){ 
												token_parking_slots.removeAt(token_index)
												for (c in TOKENID) {  
								if(  c.isDigit()  
								 ){ 
														tokenid_valid = true
														SLOTNUM = Character.getNumericValue(c)
								}
								 }  
								}
						}
						println("[businesslogic] | [State] checkTokenId | Exit point.")
					}
					 transition( edgeName="goto",targetState="pickUpCar", cond=doswitchGuarded({ tokenid_valid  
					}) )
					transition( edgeName="goto",targetState="invalidTokenId", cond=doswitchGuarded({! ( tokenid_valid  
					) }) )
				}	 
				state("invalidTokenId") { //this:State
					action { //it:State
						println("[businesslogic] | [State] invalidTokenId | Entry point.")
						println("[businesslogic] | [State] invalidTokenId | TOKENID=$TOKENID is invalid. Can't pick up the car.")
						answer("car_pickup", "accept_out_failure", "accept_out_failure(X)"   )  
						println("[businesslogic] | [State] invalidTokenId | Entry point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("pickUpCar") { //this:State
					action { //it:State
						println("[businesslogic] | [State] pickUpCar | Entry point.")
						println("[businesslogic] | [State] pickUpCar | Telling the transport trolley to pick up the car at the parking slot n...")
						request("transport_trolley_car_pickup", "transport_trolley_car_pickup($SLOTNUM)" ,"logicaltransporttrolley" )  
						 
									STATUS_PARKING_AREA[SLOTNUM - 1] = "FREE"
									SLOTNUM_PICKUP = SLOTNUM
									val P1 =  STATUS_PARKING_AREA[0]
									val P2 =  STATUS_PARKING_AREA[1]
									val P3 =  STATUS_PARKING_AREA[2]
									val P4 =  STATUS_PARKING_AREA[3]
									val P5 =  STATUS_PARKING_AREA[4]
									val P6 =  STATUS_PARKING_AREA[5]
									car_pickup_request = true
						emit("status_parking_slots", "status_parking_slots($P1,$P2,$P3,$P4,$P5,$P6)" ) 
						updateResourceRep( "status_parking_slots($P1,$P2,$P3,$P4,$P5,$P6)"  
						)
						println("[businesslogic] | [State] pickUpCar | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("pickUpCarFinished") { //this:State
					action { //it:State
						println("[businesslogic] | [State] pickUpCarFinished | Entry point.")
						println("[businesslogic] | [State] pickUpCarFinished | Set free the parking slot n. $SLOTNUM_PICKUP")
						 
									parking_slots.add(SLOTNUM_PICKUP) 
									outdoor_area_free = false
									car_pickup_request = false
						println("[businesslogic] | [State] pickUpCarFinished | Telling the client to go away...")
						answer("car_pickup", "accept_out_success", "accept_out_success(X)"   )  
						println("[businesslogic] | [State] pickUpCarFinished | Entry point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("setFreeOutdoorArea") { //this:State
					action { //it:State
						println("[businesslogic] | [State] setFreeOutdoorArea | Entry point.")
						println("[businesslogic] | [State] setFreeOutdoorArea | The sonar has notified that the car has left the outdoor area.")
						 outdoor_area_free = true  
						println("[businesslogic] | [State] setFreeOutdoorArea | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
				state("setOccupiedOutdoorArea") { //this:State
					action { //it:State
						println("[businesslogic] | [State] setOccupiedOutdoorArea | Entry point.")
						println("[businesslogic] | [State] setOccupiedOutdoorArea | The sonar has notified that the car has entered the outdoor area.")
						 outdoor_area_free = false  
						println("[businesslogic] | [State] setOccupiedOutdoorArea | Exit point.")
					}
					 transition( edgeName="goto",targetState="running", cond=doswitch() )
				}	 
			}
		}
}
