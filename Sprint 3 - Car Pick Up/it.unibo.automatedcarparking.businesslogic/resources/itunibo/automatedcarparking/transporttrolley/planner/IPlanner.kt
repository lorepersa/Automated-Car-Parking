package itunibo.automatedcarparking.transporttrolley.planner

import itunibo.automatedcarparking.transporttrolley.Position

interface IPlanner {
	fun buildPlan(destination: String) : Boolean
	
	fun clearPlan()
	
	fun setPosition(positionName : String) : Boolean
	
	fun nextMove(): String?
	
	fun moveDone()
	
	fun moveFail()
	
	fun isWaitingMoveResult() : Boolean
	
	fun getCurrentPosition(): Position
	
	fun isHomePosition(position : Position) : Boolean
	
	
}
