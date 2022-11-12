package itunibo.automatedcarparking.transporttrolley.planner

import itunibo.automatedcarparking.transporttrolley.Position

object PlannerGoalUtility {
	
	fun updateDirection(position: Position, move: String) {

		when (position.direction) {
			"upDir" -> {
				when (move) {
					"l" -> {
						position.direction = "leftDir"
					}
					"r" -> {
						position.direction = "rightDir"
					}
				}
			}
			"rightDir" -> {
				when (move) {
					"l" -> {
						position.direction = "upDir"
					}
					"r" -> {
						position.direction = "downDir"
					}
				}
			}
			"downDir" -> {
				when (move) {
					"l" -> {
						position.direction = "rightDir"
					}
					"r" -> {
						position.direction = "leftDir"
					}
				}
			}
			"leftDir" -> {
				when (move) {
					"l" -> {
						position.direction = "downDir"
					}
					"r" -> {
						position.direction = "upDir"
					}
				}
			}
		}
	}

	fun updatePosition(position: Position, move: String) {
		when (move) {
			"l", "a" -> {
				updateDirection(position, "l")
			}
			"r", "d" -> {
				updateDirection(position, "r")
			}
			"w" -> {
				when (position.direction) {
					"upDir" -> {
						position.row--
					}
					"rightdir" -> {
						position.column++
					}
					"downDir" -> {
						position.row++
					}
					"leftDir" -> {
						position.column--
					}
				}
			}
			"s" -> {
				when (position.direction) {
					"upDir" -> {
						position.row++
					}
					"rightdir" -> {
						position.column--
					}
					"downDir" -> {
						position.row--
					}
					"leftDir" -> {
						position.column++
					}
				}
			}
		}
	}

	fun addMoveToPlan(plan: MutableCollection<String>, move: String) {
		when (move) {
			"l", "a" -> {
				plan.add("l")
			}
			"r", "d" -> {
				plan.add("r")
			}
			"w" -> {
				plan.add("w")
			}
			"s" -> {
				plan.add("l")
				plan.add("l")
				plan.add("w")
				plan.add("l")
				plan.add("l")
			}
		}
	}

	fun getNextTurnMove(position: Position, goalDirection: String): String? {

		if (position.direction.equals(goalDirection)) return null

		when (position.direction) {
			"upDir" -> {
				when (goalDirection) {
					"rightDir" -> {
						return "r"
					}
					else -> {
						return "l"
					}
				}
			}
			"rightDir" -> {
				when (goalDirection) {
					"downDir" -> {
						return "r"
					}
					else -> {
						return "l"
					}
				}
			}
			"downDir" -> {
				when (goalDirection) {
					"leftDir" -> {
						return "r"
					}
					else -> {
						return "l"
					}
				}
			}
			"leftDir" -> {
				when (goalDirection) {
					"upDir" -> {
						return "r"
					}
					else -> {
						return "l"
					}
				}
			}
		}
		return null
	}

}