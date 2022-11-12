/* Generated by AN DISI Unibo */ 
package it.unibo.parkingslotscontroller

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Parkingslotscontroller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				val controller = itunibo.automatedcarparking.parkingarea.ParkingSlotController
				val resource = itunibo.qakobserver.FactoryQakResource.create(myself)
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						
									controller.init()
									resource.notify(controller.getJsonStatus())
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] wait")
					}
					 transition(edgeName="t023",targetState="handleReserveParkingSlot",cond=whenRequest("reserve_parking_slot"))
					transition(edgeName="t024",targetState="handleConfirmParkingSlot",cond=whenDispatch("confirm_parking_slot"))
					transition(edgeName="t025",targetState="handleUndoneReservation",cond=whenDispatch("undone_reservation"))
					transition(edgeName="t026",targetState="handleValidateTokenId",cond=whenRequest("validate_tokenid"))
					transition(edgeName="t027",targetState="handleFreeParkingSlot",cond=whenDispatch("free_parking_slot"))
					transition(edgeName="t028",targetState="handleReset",cond=whenDispatch("reset"))
				}	 
				state("handleReserveParkingSlot") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] handleReserveParkingSlot | Entry point.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("reserve_parking_slot(X)"), Term.createTerm("reserve_parking_slot(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  !controller.isAvailableParkingSlot()  
								 ){println("[parkingslotscontroller] | [State] handleReserveParkingSlot | Parking area is full...")
								answer("reserve_parking_slot", "no_available_parking_slots", "no_available_parking_slots(X)"   )  
								}
								else
								 { 
								 				val parkingSlot = controller.moveFromFreeToReserved() 
								 				val SLOTNUM = parkingSlot.first
								 				val TOKENID = parkingSlot.second
								 				resource.notify(controller.getJsonStatus())
								 println("[parkingslotscontroller] | [State] handleReserveParkingSlot | Reserved SLOTNUM $SLOTNUM - TOKENID $TOKENID")
								 answer("reserve_parking_slot", "parking_slot_reserved", "parking_slot_reserved($SLOTNUM,$TOKENID)"   )  
								 }
						}
						println("[parkingslotscontroller] | [State] handleReserveParkingSlot | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleConfirmParkingSlot") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] handleConfirmParkingSlot | Entry point.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("confirm_parking_slot(SLOTNUM,TOKENID)"), Term.createTerm("confirm_parking_slot(SLOTNUM,TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											val reservedSlotnum = payloadArg(0).toInt()
											println("[parkingslotscontroller] | [State] handleConfirmParkingSlot | Confirm parking slot $reservedSlotnum...") 
											controller.moveFromReservedToEngaged(reservedSlotnum)
											resource.notify(controller.getJsonStatus())
						}
						println("[parkingslotscontroller] | [State] handleConfirmParkingSlot | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleUndoneReservation") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] handleUndoneReservation | Entry point.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("undone_reservation(SLOTNUM,TOKENID)"), Term.createTerm("undone_reservation(SLOTNUM,TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											val reservedSlotnum = payloadArg(0).toInt()
											val reservedTokenId = payloadArg(1)
											println("[parkingslotscontroller] | [State] handleUndoneReservation | Undone reservation for $reservedSlotnum with TOKENID $reservedTokenId.") 
											controller.moveFromReservedToFree(reservedSlotnum)
											resource.notify(controller.getJsonStatus())
						}
						println("[parkingslotscontroller] | [State] handleUndoneReservation | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleValidateTokenId") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] handleValidateTokenId | Entry point.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("validate_tokenid(TOKENID)"), Term.createTerm("validate_tokenid(TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val TOKENID = payloadArg(0)  
								 var SLOTNUM = -1  
								 try {
													SLOTNUM = controller.checkTokenId(TOKENID)
											   } catch(ignore : Exception) {}  
								if(  SLOTNUM < 0  
								 ){println("[parkingslotscontroller] | [State] handleValidateTokenId | TokenID $TOKENID is not valid.")
								answer("validate_tokenid", "invalid_tokenid", "invalid_tokenid(X)"   )  
								}
								else
								 {println("[parkingslotscontroller] | [State] handleValidateTokenId | TokenID $TOKENID is valid.")
								 answer("validate_tokenid", "valid_tokenid", "valid_tokenid($SLOTNUM)"   )  
								 }
						}
						println("[parkingslotscontroller] | [State] handleValidateTokenId | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleFreeParkingSlot") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] handleFreeParkingSlot | Entry point.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("free_parking_slot(SLOTNUM,TOKENID)"), Term.createTerm("free_parking_slot(SLOTNUM,TOKENID)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											val SLOTNUM = payloadArg(0)
											val TOKENID = payloadArg(1)
											println("[parkingslotscontroller] | [State] handleFreeParkingSlot | Move from engaged to free parking slot $SLOTNUM.")
											controller.moveFromEngagedToFree(TOKENID)
											resource.notify(controller.getJsonStatus())
						}
						println("[parkingslotscontroller] | [State] handleFreeParkingSlot | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("handleReset") { //this:State
					action { //it:State
						println("[parkingslotscontroller] | [State] handleReset | Entry point.")
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("reset(X)"), Term.createTerm("reset(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											controller.reset()
											resource.notify(controller.getJsonStatus())
						}
						println("[parkingslotscontroller] | [State] handleReset | Exit point.")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
