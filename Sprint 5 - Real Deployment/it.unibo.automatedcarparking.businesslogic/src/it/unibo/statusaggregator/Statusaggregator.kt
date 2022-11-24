/* Generated by AN DISI Unibo */ 
package it.unibo.statusaggregator

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Statusaggregator ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				val resource = itunibo.qakobserver.FactoryQakResource.create(myself)
				
				val weightsensorObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "weightsensor", itunibo.automatedcarparking.parkingarea.AggregatorWeightSensorAutoMessage)
				val outsonarObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "outsonar", itunibo.automatedcarparking.parkingarea.AggregatorOutSonarAutoMessage)
				val thermometerObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "thermometer", itunibo.automatedcarparking.parkingarea.AggregatorThermometerAutoMessage)
				val indoorcontrollerObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "indoorcontroller", itunibo.automatedcarparking.parkingarea.AggregatorIndoorControllerAutoMessage)
				val outdoorcontrollerObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "outdoorcontroller", itunibo.automatedcarparking.parkingarea.AggregatorOutdoorControllerAutoMessage)
				val parkingslotscontrollerObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "parkingslotscontroller", itunibo.automatedcarparking.parkingarea.AggregatorParkingSlotsControllerAutoMessage)
				val thermometerfilterObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "thermometerfilter", itunibo.automatedcarparking.parkingarea.AggregatorThermometerFilterAutoMessage)
				val fancontrollerObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "fancontroller", itunibo.automatedcarparking.parkingarea.AggregatorFanControllerAutoMessage)
				val transporttrolleyObserver = itunibo.qakobserver.FactoryQakObserver.create(myself, "transporttrolley", itunibo.automatedcarparking.parkingarea.AggregatorTransportTrolleyAutoMessage)
		
				
				val status = itunibo.automatedcarparking.parkingarea.AggregateStatus()
				val gson = com.google.gson.Gson()
				
				fun getJsonStatus() : String {
					return gson.toJson(status) 
				}
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						 resource.notify(getJsonStatus())  
						 thermometerObserver.observe()  
						 indoorcontrollerObserver.observe()  
						 outdoorcontrollerObserver.observe()  
						 parkingslotscontrollerObserver.observe()  
						 thermometerfilterObserver.observe()  
						 fancontrollerObserver.observe()  
						 transporttrolleyObserver.observe()  
						 resource.notify(getJsonStatus())  
					}
					 transition( edgeName="goto",targetState="loop", cond=doswitch() )
				}	 
				state("loop") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(  status.weightsensorOn  
						 ){if( checkMsgContent( Term.createTerm("weightsensor_info_off(X)"), Term.createTerm("weightsensor_info_off(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.weightsensorOn = false  
								 weightsensorObserver.cancel()  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_weightsensor(W)"), Term.createTerm("auto_aggregator_weightsensor(W)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val W = payloadArg(0).toInt()  
								 status.indoorAreaWeight = W  
						}
						}
						else
						 {if( checkMsgContent( Term.createTerm("weightsensor_info_on(X)"), Term.createTerm("weightsensor_info_on(X)"), 
						                         currentMsg.msgContent()) ) { //set msgArgList
						 		 status.weightsensorOn = true  
						 		 weightsensorObserver.observe()  
						 }
						 }
						if(  status.outsonarOn  
						 ){if( checkMsgContent( Term.createTerm("outsonar_info_off(X)"), Term.createTerm("outsonar_info_off(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.outsonarOn = false  
								 outsonarObserver.cancel()  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_outsonar(D)"), Term.createTerm("auto_aggregator_outsonar(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = payloadArg(0).toInt()  
								 status.outdoorAreaDistance = D  
						}
						}
						else
						 {if( checkMsgContent( Term.createTerm("outsonar_info_on(X)"), Term.createTerm("outsonar_info_on(X)"), 
						                         currentMsg.msgContent()) ) { //set msgArgList
						 		 status.outsonarOn = true  
						 		 outsonarObserver.observe()  
						 }
						 }
						if( checkMsgContent( Term.createTerm("auto_aggregator_thermometer(T)"), Term.createTerm("auto_aggregator_thermometer(T)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val T = payloadArg(0).toInt()  
								 status.parkingAreaTemperature = T  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_indoorcontroller(RESERVED,ENGAGEDBYCAR,CARENTERTIMEOUTALARM)"), Term.createTerm("auto_aggregator_indoorcontroller(RESERVED,ENGAGEDBYCAR,CARENTERTIMEOUTALARM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.indoorAreaReserved = payloadArg(0).toBoolean()  
								 status.indoorAreaEngaged = payloadArg(1).toBoolean()  
								 status.indoorAreaCarEnterTimeoutAlarm = payloadArg(2).toBoolean()  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_outdoorcontroller(RESERVED,ENGAGEDBYCAR,DTFREETIMEOUTALARM)"), Term.createTerm("auto_aggregator_outdoorcontroller(RESERVED,ENGAGEDBYCAR,DTFREETIMEOUTALARM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.outdoorAreaReserved = payloadArg(0).toBoolean()  
								 status.outdoorAreaEngaged = payloadArg(1).toBoolean()  
								 status.outdoorAreaDTFREETimeoutAlarm = payloadArg(2).toBoolean()  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_parkingslotscontroller(P1,P2,P3,P4,P5,P6)"), Term.createTerm("auto_aggregator_parkingslotscontroller(P1,P2,P3,P4,P5,P6)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												status.parkingSlotsStatus.clear()
												for (i in 1..6) {
													val PiStatus = payloadArg(i-1)
													val parkingSlot = itunibo.automatedcarparking.parkingarea.ParkingSlot(i, itunibo.automatedcarparking.parkingarea.Status(PiStatus))
													status.parkingSlotsStatus.add(parkingSlot)
												}
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_thermometerfilter(HIGH)"), Term.createTerm("auto_aggregator_thermometerfilter(HIGH)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.parkingAreaTemperatureHigh = payloadArg(0).toBoolean()  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_fancontroller(ON,AUTOMATIC,FAILUREREASON)"), Term.createTerm("auto_aggregator_fancontroller(ON,AUTOMATIC,FAILUREREASON)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.fanOn = payloadArg(0).toBoolean()  
								 status.fanAutomatic = payloadArg(1).toBoolean()  
								 status.fanFailureReason = payloadArg(2)  
						}
						if( checkMsgContent( Term.createTerm("auto_aggregator_transporttrolley(STOPPED,IDLE,MOVEFAILED,COLUMN,ROW,DIRECTION)"), Term.createTerm("auto_aggregator_transporttrolley(STOPPED,IDLE,MOVEFAILED,COLUMN,ROW,DIRECTION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 status.transportTrolleyStopped = payloadArg(0).toBoolean()  
								 status.transportTrolleyIdle = payloadArg(1).toBoolean()  
								 status.transportTrolleyMoveFailed = payloadArg(2).toBoolean()  
								 status.transportTrolleyCoordinate.column = payloadArg(3).toInt()  
								 status.transportTrolleyCoordinate.row = payloadArg(4).toInt()  
								 status.transportTrolleyCoordinate.direction = payloadArg(5)  
						}
						 resource.notify(getJsonStatus())  
					}
					 transition(edgeName="t049",targetState="loop",cond=whenDispatch("weightsensor_info_on"))
					transition(edgeName="t050",targetState="loop",cond=whenDispatch("weightsensor_info_off"))
					transition(edgeName="t051",targetState="loop",cond=whenDispatch("outsonar_info_on"))
					transition(edgeName="t052",targetState="loop",cond=whenDispatch("outsonar_info_off"))
					transition(edgeName="t053",targetState="loop",cond=whenDispatch("auto_aggregator_weightsensor"))
					transition(edgeName="t054",targetState="loop",cond=whenDispatch("auto_aggregator_outsonar"))
					transition(edgeName="t055",targetState="loop",cond=whenDispatch("auto_aggregator_thermometer"))
					transition(edgeName="t056",targetState="loop",cond=whenDispatch("auto_aggregator_indoorcontroller"))
					transition(edgeName="t057",targetState="loop",cond=whenDispatch("auto_aggregator_outdoorcontroller"))
					transition(edgeName="t058",targetState="loop",cond=whenDispatch("auto_aggregator_parkingslotscontroller"))
					transition(edgeName="t059",targetState="loop",cond=whenDispatch("auto_aggregator_thermometerfilter"))
					transition(edgeName="t060",targetState="loop",cond=whenDispatch("auto_aggregator_fancontroller"))
					transition(edgeName="t061",targetState="loop",cond=whenDispatch("auto_aggregator_transporttrolley"))
				}	 
			}
		}
}