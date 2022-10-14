package itunibo.automatedcarparking.transporttrolley.basicrobot

import it.unibo.kactor.ActorBasicFsm
import kotlinx.coroutines.delay

object BasicRobotUtility {
	
	private var STEP_TIME = 350L
	private val TURN_MOVE_DURATION = 300L
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi		
	suspend fun sendMove(actor : ActorBasicFsm, move : String) {
		
		when (move) {
			"l", "r" -> {
				actor.forward("cmd", "cmd($move)", "basicrobot" )
				delay(TURN_MOVE_DURATION) 
				actor.autoMsg("move_done", "move_done($move)")  
			}
			"w" -> {
				actor.request("step", "step($STEP_TIME)", "basicrobot" )  
			}
		}
	}
	
	fun setStepMoveDuration(duration : Long) {
		STEP_TIME = duration
	}
}