package itunibo.automatedcarparking.transporttrolley.planner

import itunibo.planner.plannerUtil
import itunibo.automatedcarparking.rolodex.TransportTrolleyRolodex
import itunibo.automatedcarparking.transporttrolley.Position
import itunibo.automatedcarparking.transporttrolley.NamedPositionCollection
import itunibo.automatedcarparking.rolodex.MapRolodex
import itunibo.automatedcarparking.configuration.ParkingAreaMapReader
import itunibo.automatedcarparking.configuration.MapNamedPositionsReader


object SingletonPlanner : IPlanner {

	private lateinit var knownPositions: NamedPositionCollection
	private val currentPlan = mutableListOf<String>()
	private var currentMove: String? = null
	private var postponedDestination: Position? = null
	private var initialized = false
	private lateinit var homePosition : Position

	fun init() {
		if (!initialized) {
			ParkingAreaMapReader.generateRoomMapBin()
			plannerUtil.initAI()
			plannerUtil.loadRoomMap(MapRolodex.filenameMap)
			knownPositions =
				MapNamedPositionsReader.read(TransportTrolleyRolodex.filenameNamedPositions)

			homePosition = knownPositions.get("HOME")!!
			initialized = true
		}
	}

	private fun generatePlan(goalPosition: Position) {
		plannerUtil.setGoal(goalPosition.column, goalPosition.row)
		val actions = plannerUtil.doPlan()
		val position = getCurrentPosition()
		actions?.forEach {
			val move = it.toString()
			PlannerGoalUtility.updatePosition(position, move)
			PlannerGoalUtility.addMoveToPlan(currentPlan, move)
		}

		do {
			val turnMove = PlannerGoalUtility.getNextTurnMove(position, goalPosition.direction)

			turnMove?.let {
				PlannerGoalUtility.updatePosition(position, turnMove)
				PlannerGoalUtility.addMoveToPlan(currentPlan, turnMove)
			}
		} while (turnMove != null)
		
		println("NEW PLAN: $currentPlan")

		postponedDestination = null
	}

	override fun buildPlan(destination: String): Boolean {
		postponedDestination = null
		val goal = knownPositions.get(destination)

		goal?.let {
			currentPlan.clear()
			if (currentMove != null) {
				// there is a move that we do not know if successed or failed
				// need to postpone the buildPlan at a later time
				// (when we know the result of the move)
				postponedDestination = it
			} else {
				generatePlan(goal)
			}
		}

		return goal != null
	}
	
	override fun clearPlan() {
		currentPlan.clear()
		currentMove = null
		postponedDestination = null
	}
	
	override fun setPosition(positionName : String) : Boolean {
		clearPlan()
		
		if (buildPlan(positionName)) {
			currentPlan.forEach {
				currentMove = it
				moveDone()
			}
			clearPlan()
		}
		
		return false
		
	}

	override fun nextMove(): String? {
		if (currentMove != null) {
			return currentMove
		}

		currentMove = currentPlan.removeFirstOrNull()
		return currentMove
	}

	override fun moveDone() {
		currentMove?.let {
			plannerUtil.updateMap(it)
		}
		postponedDestination?.let {
			generatePlan(it)
		}
		currentMove = null
	}

	override fun moveFail() {
		currentMove?.let {
			currentPlan.add(0, it)
		}
		postponedDestination?.let {
			generatePlan(it)
		}
		currentMove = null
	}
	
	override fun isWaitingMoveResult() : Boolean {
		return currentMove != null
	}

	override fun getCurrentPosition(): Position {
		val X = plannerUtil.get_curPos().first
		val Y = plannerUtil.get_curPos().second
		val dir = plannerUtil.getDirection()
		return Position(X, Y, dir)
	}
	
	override fun isHomePosition(position : Position) : Boolean {
		return position == homePosition
	}
}
